package team.alpha.model;

public class InstanceStats {
    private int requestsCount = 0;


    public int getRequestsCount() {
        return requestsCount;
    }

    public void incrementRequestsCount() {
        this.requestsCount = this.requestsCount + 1;
    }


}
