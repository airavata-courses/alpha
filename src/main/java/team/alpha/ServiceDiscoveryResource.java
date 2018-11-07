package team.alpha;

import clover.com.google.gson.Gson;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.*;
import org.apache.zookeeper.CreateMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.alpha.model.InstanceDetails;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static team.alpha.Configurations.ZK_BASEPATH;
import static team.alpha.Configurations.ZK_CONNECTION;

@RestController
public class ServiceDiscoveryResource {

    private final CuratorFramework curatorFramework;
    private final ServiceDiscovery serviceDiscovery;
    private final Map<String, ServiceProvider> serviceProviderMap = new HashMap<>();
    private final Gson gson = new Gson();

    public ServiceDiscoveryResource() {
        try {
            //start framework
            curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
            curatorFramework.start();

            serviceDiscovery = ServiceDiscoveryBuilder
                    .builder(Void.class)
                    .basePath(ZK_BASEPATH)
                    .client(curatorFramework).build();

            serviceDiscovery.start();

        } catch (Exception e) {
            throw new RuntimeException("Curator Framework could not be started\n" + e.getLocalizedMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/service", method = RequestMethod.PUT)
    ResponseEntity<?> registerService(@RequestBody InstanceDetails instanceDetails) {
        ResponseEntity responseEntity;
        try {
            String znodePath = ZK_BASEPATH + instanceDetails.getServiceName() + "/" + instanceDetails.getInstanceId();
            curatorFramework.delete().inBackground().forPath(znodePath);

            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .address(instanceDetails.getAddress())
                    .port(instanceDetails.getPort())
                    .name(instanceDetails.getServiceName())
                    .id(instanceDetails.getInstanceId())
                    .uriSpec(new UriSpec())
                    .build();

            Type serviceInstanceType = new TypeToken<ServiceInstance<Object>>() {
            }.getType();

            curatorFramework.create().
                    creatingParentsIfNeeded().
                    withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
                    forPath(znodePath, gson.toJson(serviceInstance, serviceInstanceType).getBytes());

            responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Failed to register service");
            e.printStackTrace();
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @CrossOrigin
    @RequestMapping(value = "/service/{name}", method = RequestMethod.GET)
    ResponseEntity<InstanceDetails> getServiceInstance(@PathVariable("name") String name) {
        try {
            ServiceProvider serviceProvider = getServiceProviderForService(name);

            ServiceInstance serviceInstance = serviceProvider.getInstance();

            if(serviceInstance == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            InstanceDetails instanceDetails = InstanceDetails.getBuilder()
                    .withServiceName(serviceInstance.getName())
                    .withInstanceId(serviceInstance.getId())
                    .withAddress(serviceInstance.getAddress())
                    .withPort(serviceInstance.getPort())
                    .build();

            return new ResponseEntity<>(instanceDetails, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Failed to get service");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ServiceProvider getServiceProviderForService(String name) throws Exception {
        ServiceProvider serviceProvider = null;
        if (serviceProviderMap.containsKey(name)) {
            serviceProvider = serviceProviderMap.get(name);
        } else {
            serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(name).build();
            serviceProvider.start();
            serviceProviderMap.put(name, serviceProvider);
        }

        return serviceProvider;
    }

}