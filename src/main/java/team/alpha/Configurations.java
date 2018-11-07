package team.alpha;

import java.io.IOException;
import java.util.Properties;

public abstract class Configurations {
    private static Properties configProperties = new Properties();

    static {
        try {
            configProperties.load(Configurations.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Configurations could not loaded from properties file", e);
        }
    }

    private static final String ZK_HOST = configProperties.getProperty("zookeeper.host");
    private static final String ZK_PORT = configProperties.getProperty("zookeeper.port");
    public static final String ZK_CONNECTION = ZK_HOST + ":" + ZK_PORT;
    public static final String ZK_BASEPATH = "/services";

    //instance cleanup
    public static final int CLEANUP_PERIOD_MS = 5000;
    public static final int INSTANCE_REFRESH_PERIOD_MS = Integer.parseInt(configProperties.getProperty("instance_refresh_time_ms"));

}
