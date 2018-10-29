package team.alpha;

public class Configuration {
    public static final int DEFAULT_JETTY_PORT = 8081;
    private static final String ZK_BASEPATH = "/";
    private static final String ZK_CONNECTION = "127.0.0.1:2181";
    private static final int INSTANCE_REFRESH_TIME_MS = 30000;

    public String getZKConnection() {
        return ZK_CONNECTION;
    }

    public String getZKBasePath() {
        return ZK_BASEPATH;
    }

    public int getInstanceRefreshTimeMs() {
        return INSTANCE_REFRESH_TIME_MS;
    }

    public int getJettyPort(){
        return DEFAULT_JETTY_PORT;
    }
}
