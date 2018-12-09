package team.alpha;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.ServiceStats;

import java.net.URI;
import java.util.HashMap;
import java.util.Random;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class LoadBalancerResource {

    private static final String[] SERVICE_NAMES = {"news", "stocks", "weather"};
    private Random randomGenerator = new Random();
    private PoolingHttpClientConnectionManager cm;
    private CloseableHttpClient httpClient;
    private final HashMap<String, ServiceStats> statistics = new HashMap<>();

    public LoadBalancerResource() {

        try {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            httpClient = HttpClients.custom().setConnectionManager(cm).build();

            for (String serviceName : SERVICE_NAMES) {
                statistics.put(serviceName, new ServiceStats());
            }

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

                URI newsURI = new URIBuilder()
                        .setScheme("http")
                        .setHost("news")
                        .setPort(5000)
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

                URI stocksURI = new URIBuilder()
                        .setScheme("http")
                        .setHost("stocks")
                        .setPort(8000)
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

                URI weatherURI = new URIBuilder()
                        .setScheme("http")
                        .setHost("weather")
                        .setPort(9102)
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

            return new ResponseEntity<>(statistics, OK);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return new ResponseEntity<>(statistics, INTERNAL_SERVER_ERROR);
    }

}
