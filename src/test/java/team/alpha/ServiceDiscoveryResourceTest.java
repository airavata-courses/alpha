package team.alpha;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import team.alpha.model.InstanceDetails;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceDiscoveryResourceTest {

    private static ServiceDiscoveryResource serviceDiscoveryResource = new ServiceDiscoveryResource();

    @Test
    public void test1registerService() {

        InstanceDetails instanceDetails = InstanceDetails.getBuilder()
                .withServiceName("db")
                .withInstanceId("_1")
                .withAddress("10.10.10.10")
                .withPort(9102)
                .build();

        serviceDiscoveryResource.registerService(instanceDetails);
    }

    @Test
    public void test2discoverService() throws InterruptedException {
//        Thread.sleep(1000);
        serviceDiscoveryResource.getServiceInstance("db");
    }
}
