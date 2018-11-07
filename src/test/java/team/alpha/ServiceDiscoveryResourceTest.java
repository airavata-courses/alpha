package team.alpha;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<?> responseEntity = serviceDiscoveryResource.registerService(instanceDetails);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void test2discoverService() throws InterruptedException {
        Thread.sleep(2000);
        ResponseEntity<?> responseEntity = serviceDiscoveryResource.getServiceInstance("db");
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
