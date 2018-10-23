package team.alpha;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

    private static Properties configProperties = new Properties();

    static {
        try {
            InputStream inputStream = Constants.class.getResourceAsStream("/config.properties");
            configProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Constants() {
        //cant be instantiated
    }

    public static final String DB_HOSTURL = configProperties.getProperty("db.hosturl");
    public static final String DB_PORT = configProperties.getProperty("db.port");
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    public static final String DB_URL = "jdbc:postgresql://" + DB_HOSTURL + ":" + DB_PORT + "/activedb?user=" + DB_USER + "&password=" + DB_PASSWORD;

    //error messages
    public static final String MSG_INVALID_CREDENTIALS = "Either username or password is invalid";
    public static final String MSG_FAILED_TO_FETCH_USER = "Error while fetching user data";
    public static final String MSG_FAILED_TO_CREATE_USER = "Error while creating user data";
    public static final String MSG_USER_ALREADY_EXISTS = "That username has been taken. Please try another one.";
    public static final String MSG_FAILED_TO_FETCH_SUBSCRIBER_LIST = "Error while fetching subscribers list";

}
