package team.alpha;

public class Configuration {
    private static final String ZK_BASEPATH = "/services";
    private static final String ZK_CONNECTION = "127.0.0.1:2181";

    public String getZKConnection() {
        return ZK_CONNECTION;
    }

    public String getZKBasePath() {
        return ZK_BASEPATH;
    }
}
