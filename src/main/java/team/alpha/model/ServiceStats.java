package team.alpha.model;

import java.util.HashMap;
import java.util.Map;

public class ServiceStats {
    private final Map<String, InstanceStats> instances = new HashMap<>();
    private int requestsCount = 0;
    private int requestsFailed = 0;

    public Map<String, InstanceStats> getInstances() {
        return instances;
    }

    public int getRequestsCount() {
        return requestsCount;
    }

    public void incrementRequestsCount() {
        this.requestsCount = this.requestsCount + 1;
    }

    public int getRequestsFailed() {
        return requestsFailed;
    }


    public void incrementRequestsFailed() {
        this.requestsFailed = this.requestsFailed + 1;
    }
}
