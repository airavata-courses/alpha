package team.alpha.model;

public class ServiceStats {
    private int requestsCount = 0;
    private int requestsFailed = 0;

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
