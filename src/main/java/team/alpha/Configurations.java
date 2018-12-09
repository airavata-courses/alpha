package team.alpha;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Configurations {

    private static Properties configProperties = new Properties();

    static {
        try {
            InputStream inputStream = Configurations.class.getResourceAsStream("/application.properties");
            configProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Configurations could not loaded from properties file");
        }
    }

    private Configurations() {
        //cant be instantiated
    }

    //database
    public static final String DB_HOSTURL = configProperties.getProperty("db.hosturl");
    public static final String DB_PORT = configProperties.getProperty("db.port");
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_URL = "jdbc:postgresql://"+ DB_HOSTURL+":"+ DB_PORT +"/activedb?user=" + DB_USER + "&password=" + DB_PASSWORD;

}
