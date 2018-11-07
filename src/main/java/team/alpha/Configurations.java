package team.alpha;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

public abstract class Configurations {

    private static Properties configProperties = new Properties();

    public static final String APPLIATION_HOST;

    static {
        try {
            InputStream inputStream = Configurations.class.getResourceAsStream("/application.properties");
            configProperties.load(inputStream);
            APPLIATION_HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Configurations could not loaded from properties file");
        }
    }

    private Configurations() {
        //cant be instantiated
    }

    //application properties
    public static final int APPLICATION_PORT = Integer.parseInt(configProperties.getProperty("server.port"));

    //database
    public static final String DB_HOSTURL = configProperties.getProperty("db.hosturl");
    public static final String DB_PORT = configProperties.getProperty("db.port");
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_URL = "jdbc:postgresql://" + DB_HOSTURL + ":" + DB_PORT + "/activedb?user=" + DB_USER + "&password=" + DB_PASSWORD;

    //service discovery
    private static final String ZK_HOST = configProperties.getProperty("zookeeper.host");
    private static final String ZK_PORT = configProperties.getProperty("zookeeper.port");
    public static final String ZK_CONNECTION = ZK_HOST + ":" + ZK_PORT;
    public static final String ZK_BASEPATH = "/services";
    public static final String SERVICE_NAME = configProperties.getProperty("service_name");
    public static final String INSTANCE_ID = configProperties.getProperty("instance_id");

}
