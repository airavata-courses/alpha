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
//    static final int DEFAULT_SESSION_TIMEOUT_MS = 60000;
//    private static final int DEFAULT_CONNECTION_TIMEOUT_MS =
//            configProperties.getProperty("connection_timeout_ms") != null ? Integer.parseInt(configProperties.getProperty("connection_timeout_ms")) : 30000;
    public static final String ZK_BASEPATH = "/";

}
