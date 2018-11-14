package team.alpha;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.InstanceStats;
import team.alpha.model.ServiceStats;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static team.alpha.Configurations.*;

@RestController
public class LoadBalancerResource {

    private final CuratorFramework curatorFramework;
    private final ServiceDiscovery<Object> serviceDiscovery;
    private final Map<String, ServiceCache<Object>> serviceCaches = new HashMap<>();
    private Random randomGenerator = new Random();
    private PoolingHttpClientConnectionManager cm;
    private CloseableHttpClient httpClient;
    private final HashMap<String, ServiceStats> statistics = new HashMap<>();

    public LoadBalancerResource() {
        try {
            curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
            curatorFramework.start();

            serviceDiscovery = ServiceDiscoveryBuilder
                    .builder(Object.class)
                    .serializer(new JsonInstanceSerializer<>(Object.class))
                    .basePath(ZK_BASEPATH)
                    .client(curatorFramework).build();
            serviceDiscovery.start();

            for (String serviceName : SERVICE_NAMES) {
                ServiceCache<Object> serviceCache = serviceDiscovery.serviceCacheBuilder().name(serviceName).build();
                serviceCache.start();
                serviceCaches.put(serviceName, serviceCache);
                statistics.put(serviceName, new ServiceStats());
            }

            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            httpClient = HttpClients.custom().setConnectionManager(cm).build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Load Balancer Resource object\n" + e.getLocalizedMessage());
        }

    }

    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    ResponseEntity<?> testServices() {

        try {
            GetRequestThread[] requestThreads = new GetRequestThread[3];

            try {
                statistics.get("news").incrementRequestsCount();
                ServiceInstance<Object> newsServiceInstance = getRandomInstance("news");
                Preconditions.checkNotNull(newsServiceInstance);
                if (!statistics.get("news").getInstances().containsKey(newsServiceInstance.getId())) {
                    statistics.get("news").getInstances().put(newsServiceInstance.getId(), new InstanceStats());
                }
                statistics.get("news").getInstances().get(newsServiceInstance.getId()).incrementRequestsCount();

                URI newsURI = new URIBuilder()
                        .setScheme("http")
                        .setHost(newsServiceInstance.getAddress())
                        .setPort(newsServiceInstance.getPort())
                        .setPath("/top_headlines")
                        .build();
                requestThreads[0] = new GetRequestThread(httpClient, newsURI);
                requestThreads[0].start();
            } catch (Exception e) {
                statistics.get("news").incrementRequestsFailed();
                System.out.println(e.getLocalizedMessage());
            }

            try {
                statistics.get("stocks").incrementRequestsCount();
                ServiceInstance<Object> stocksServiceInstance = getRandomInstance("stocks");
                if (!statistics.get("stocks").getInstances().containsKey(stocksServiceInstance.getId())) {
                    statistics.get("stocks").getInstances().put(stocksServiceInstance.getId(), new InstanceStats());
                }
                statistics.get("stocks").getInstances().get(stocksServiceInstance.getId()).incrementRequestsCount();

                URI stocksURI = new URIBuilder()
                        .setScheme("http")
                        .setHost(stocksServiceInstance.getAddress())
                        .setPort(stocksServiceInstance.getPort())
                        .setPath("/stocks/apple")
                        .build();
                requestThreads[1] = new GetRequestThread(httpClient, stocksURI);
                requestThreads[1].start();
            } catch (Exception e) {
                statistics.get("stocks").incrementRequestsFailed();
                System.out.println(e.getLocalizedMessage());
            }

            try {
                statistics.get("weather").incrementRequestsCount();
                ServiceInstance<Object> weatherServiceInstance = getRandomInstance("weather");
                if (!statistics.get("weather").getInstances().containsKey(weatherServiceInstance.getId())) {
                    statistics.get("weather").getInstances().put(weatherServiceInstance.getId(), new InstanceStats());
                }
                statistics.get("weather").getInstances().get(weatherServiceInstance.getId()).incrementRequestsCount();

                URI weatherURI = new URIBuilder()
                        .setScheme("http")
                        .setHost(weatherServiceInstance.getAddress())
                        .setPort(weatherServiceInstance.getPort())
                        .setPath("/data")
                        .build();
                requestThreads[2] = new GetRequestThread(httpClient, weatherURI);
                requestThreads[2].start();
            } catch (Exception e) {
                statistics.get("weather").incrementRequestsFailed();
                System.out.println(e.getLocalizedMessage());
            }

            for (GetRequestThread requestThread : requestThreads) {
                requestThread.join();
            }

            return new ResponseEntity(statistics, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ServiceInstance<Object> getRandomInstance(String serviceName) {
        List<ServiceInstance<Object>> serviceInstanceList = serviceCaches.get(serviceName).getInstances();
        Preconditions.checkState(serviceInstanceList != null && serviceInstanceList.size() > 0);
        return serviceInstanceList.get(randomGenerator.nextInt(serviceInstanceList.size()));
    }
}
