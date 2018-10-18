package team.alpha;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

    private static Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = Constants.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Constants() {
        //cant be instantiated
    }

    public static final int STATUS_OK = 200;
    public static final String DB_HOSTURL = properties.getProperty("dbserver.hosturl");
    public static final String DB_PORT = properties.getProperty("dbserver.port");
    public static final String DB_URL = "jdbc:postgresql://" +  DB_HOSTURL +  ":" + DB_PORT + "/activedb";

    //error messages
    public static final String MSG_INVALID_CREDENTIALS = "Either username or password is invalid";
    public static final String MSG_FAILED_TO_FETCH_USER = "Error while fetching user data";
    public static final String MSG_FAILED_TO_CREATE_USER = "Error while creating user data";
    public static final String MSG_USER_ALREADY_EXISTS = "That username has been taken. Please try another one.";

}
