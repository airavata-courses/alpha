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
    public static final String[] SERVICE_NAMES = {"news", "weather"};
}
