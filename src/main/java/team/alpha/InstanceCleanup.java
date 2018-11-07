package team.alpha;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ThreadUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static team.alpha.Configurations.*;

public class InstanceCleanup implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(InstanceCleanup.class);
    private CuratorFramework curatorFramework;
    private ServiceDiscovery<Object> serviceDiscovery;
    private final ScheduledExecutorService cleanupService = ThreadUtils.newSingleThreadScheduledExecutor("InstanceCleanup");

    public InstanceCleanup() {
        try {
            curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
            curatorFramework.start();

            serviceDiscovery = ServiceDiscoveryBuilder
                    .builder(Object.class)
                    .serializer(new JsonInstanceSerializer<>(Object.class))
                    .basePath(ZK_BASEPATH)
                    .client(curatorFramework).build();

            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate InstanceCleanup object\n" + e.getLocalizedMessage());
        }
    }


    public void start() {
        Preconditions.checkArgument(!cleanupService.isShutdown(), "already started");
        cleanupService.scheduleWithFixedDelay(this::doWork, CLEANUP_PERIOD_MS, CLEANUP_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        Preconditions.checkArgument(!cleanupService.isShutdown(), "not started");
        cleanupService.shutdownNow();
    }

    private void doWork() {
        try {
            for (String name : serviceDiscovery.queryForNames()) {
                checkService(name);
            }
        } catch (Exception e) {
            ThreadUtils.checkInterrupted(e);
            log.error("GC for cleanupService names", e);
        }
    }

    private void checkService(String name) {
        try {
            Collection<ServiceInstance<Object>> instances = serviceDiscovery.queryForInstances(name);
            for (ServiceInstance<Object> instance : instances) {
                if (instance.getServiceType() == ServiceType.STATIC) {
                    if ((System.currentTimeMillis() - instance.getRegistrationTimeUTC()) > INSTANCE_REFRESH_PERIOD_MS) {
                        String znodePath = ZK_BASEPATH + "/" + instance.getName() + "/" + instance.getId();
                        curatorFramework.delete().inBackground().forPath(znodePath);
                    }
                }
            }
        } catch (Exception e) {
            ThreadUtils.checkInterrupted(e);
            log.error(String.format("GC for cleanupService: %s", name), e);
        }
    }
}