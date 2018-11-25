package team.alpha;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.spy;
import static team.alpha.Configurations.ZK_BASEPATH;
import static team.alpha.Configurations.ZK_CONNECTION;
import static team.alpha.Constants.BLOOMINGTON;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherResourceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    private WeatherResource weatherResource;

    @Before
    public void setUp() {
        weatherResource = spy(WeatherResource.class);
    }

    @Test
    public void test2GetData() {
        ResponseEntity response = weatherResource.getData(BLOOMINGTON);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test3Application() throws Exception {
        App.main(new String[]{});
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
        curatorFramework.start();

        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .basePath(ZK_BASEPATH)
                .client(curatorFramework)
                .build();

        serviceDiscovery.start();
        ServiceProvider dbServiceProvider = serviceDiscovery.serviceProviderBuilder().serviceName("weather").build();
        dbServiceProvider.start();

        System.out.println("Attempt three tries to get weather service instance from service registry");
        System.out.print("First try - ");
        Thread.sleep(2000);
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");

        Thread.sleep(10000);
        System.out.print("Second try - ");
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");

        Thread.sleep(10000);
        System.out.print("Third try - ");
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");

    }
}