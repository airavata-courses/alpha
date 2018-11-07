package team.alpha;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static team.alpha.Configurations.*;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        //run the application
        SpringApplication.run(App.class, args);
        registerThisService();
    }

    private static void registerThisService() {
        try {
            CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
            curatorFramework.start();

            ServiceInstance<Object> serviceInstance = ServiceInstance.builder()
                    .address(APPLICATION_HOST)
                    .port(APPLICATION_PORT)
                    .name(SERVICE_NAME)
                    .id(INSTANCE_ID)
                    .build();

            ServiceDiscoveryBuilder.builder(Object.class)
                    .basePath(ZK_BASEPATH)
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        } catch (Exception e) {
            throw new RuntimeException("Failed to register the instance to service registry\n" + e);
        }

    }
}
