package team.alpha;

import com.google.common.collect.Sets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.curator.x.discovery.details.ServiceDiscoveryImpl;
import org.apache.curator.x.discovery.server.contexts.StringDiscoveryContext;
import org.apache.curator.x.discovery.server.entity.JsonServiceInstanceMarshaller;
import org.apache.curator.x.discovery.server.entity.JsonServiceInstancesMarshaller;
import org.apache.curator.x.discovery.server.entity.JsonServiceNamesMarshaller;
import org.apache.curator.x.discovery.server.rest.InstanceCleanup;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;

import java.util.Set;

public class Application extends javax.ws.rs.core.Application {

    private StringDiscoveryContext context;
    private InstanceCleanup instanceCleanup;
    private JsonServiceNamesMarshaller serviceNamesMarshaller = new JsonServiceNamesMarshaller();
    private JsonServiceInstanceMarshaller<String> serviceInstanceMarshaller;
    private JsonServiceInstancesMarshaller<String> serviceInstancesMarshaller;

    public Application(Configuration configuration) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                configuration.getZKConnection(), new RetryNTimes(5, 1000));
        curatorFramework.start();

        ServiceDiscovery<String> serviceDiscovery = new ServiceDiscoveryImpl<String>(
                curatorFramework,
                configuration.getZKBasePath(),
                new JsonInstanceSerializer<String>(String.class),
                null, false);

        context = new StringDiscoveryContext(
                serviceDiscovery,
                new RoundRobinStrategy<String>(),
                configuration.getInstanceRefreshTimeMs());

        instanceCleanup = new InstanceCleanup(serviceDiscovery,
                configuration.getInstanceRefreshTimeMs());
        instanceCleanup.start();

        serviceInstanceMarshaller = new JsonServiceInstanceMarshaller<String>(context);
        serviceInstancesMarshaller = new JsonServiceInstancesMarshaller<String>(context);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = Sets.newHashSet();
        classes.add(ServiceDiscoveryResource.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = Sets.newHashSet();
        singletons.add(context);
        singletons.add(instanceCleanup);
        singletons.add(serviceNamesMarshaller);
        singletons.add(serviceInstanceMarshaller);
        singletons.add(serviceInstancesMarshaller);
        return singletons;
    }
}
