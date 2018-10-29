package team.alpha;

import org.apache.curator.x.discovery.server.rest.DiscoveryContext;
import org.apache.curator.x.discovery.server.rest.DiscoveryResource;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

@Path("/")
public class ServiceDiscoveryResource extends DiscoveryResource<String> {
    public ServiceDiscoveryResource(@Context ContextResolver<DiscoveryContext<String>> resolver) {
        super(resolver.getContext(DiscoveryContext.class));
    }
}
