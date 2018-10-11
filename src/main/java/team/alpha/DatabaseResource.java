package team.alpha;

import clover.com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.ErrorResponse;
import team.alpha.model.UserPreferences;
import team.alpha.model.UserSignupForm;

import java.sql.*;
import java.util.Properties;

@RestController
class DatabaseResource {

    private Gson gson = new Gson();
    private Connection db;
    private PreparedStatement loginQuery;
    private PreparedStatement userPrefQuery;
    private CallableStatement signupProc;


    DatabaseResource() {
        try {
            db = createDBConnection();
            createPreparedStatements();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Connection createDBConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        return DriverManager.getConnection(Constants.DB_URL, properties);
    }

    private void createPreparedStatements() throws SQLException {
        loginQuery = db.prepareStatement("select userid from userinfo where username = ? and password = ?");
        userPrefQuery = db.prepareStatement("select city, country, company, get_news_alerts, get_weather_alerts from userpref where userid = ?");
        signupProc = db.prepareCall("{ ? = call create_user(?,?,?,?,?,?,?) }");
        signupProc.registerOutParameter(1, Types.INTEGER);
    }

    @CrossOrigin
    @RequestMapping("/login")
    int login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        int userId = -1;

        try {

            loginQuery.setString(1, username);
            loginQuery.setString(2, password);

            ResultSet rs = loginQuery.executeQuery();

            while (rs.next()) {
                userId = rs.getInt(1);
            }

            if (userId == -1) {
                System.out.println(Constants.MSG_INVALID_CREDENTIAL);
            }

            rs.close();
            loginQuery.close();
        } catch (SQLException e) {
            System.out.println(Constants.MSG_FAILED_TO_FETCH_USER);
            e.printStackTrace();
        }

        return userId;
    }

    @CrossOrigin
    @RequestMapping("/userpref")
    String getUserPref(
            @RequestParam(value = "userid") int userId) {

        String response;

        try {
            userPrefQuery.setInt(1, userId);

            ResultSet rs = userPrefQuery.executeQuery();

            UserPreferences userPreferences = null;
            while (rs.next()) {
                userPreferences = new UserPreferences();
                userPreferences.setCity(rs.getString(1));
                userPreferences.setCountry(rs.getString(2));
                userPreferences.setCompany(rs.getString(3));
                userPreferences.setSubscribedToNewsAlerts(rs.getBoolean(4));
                userPreferences.setSubscribedToWeatherAlerts(rs.getBoolean(5));
            }

            rs.close();
            userPrefQuery.close();

            if (userPreferences == null) {
                return getErrorResponse(Constants.MSG_USER_PREF_NOT_AVAILABLE);
            }

            return gson.toJson(userPreferences);

        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(Constants.MSG_FAILED_TO_FETCH_USER_PREF);
        }

    }

    @CrossOrigin
    @RequestMapping("/signup")
    String signup(@RequestParam(value = "form") String form) {
        String response = "false";

        try {
            UserSignupForm userSignupForm = gson.fromJson(form, UserSignupForm.class);

            signupProc.setString(2, userSignupForm.getUsername());
            signupProc.setString(3, userSignupForm.getPassword());
            UserPreferences userPreferences = userSignupForm.getUserPreferences();
            signupProc.setString(4, userPreferences.getCity());
            signupProc.setString(5, userPreferences.getCountry());
            signupProc.setString(6, userPreferences.getCompany());
            signupProc.setBoolean(7, userPreferences.isSubscribedToNewsAlerts());
            signupProc.setBoolean(8, userPreferences.isSubscribedToWeatherAlerts());

            signupProc.execute();
            int userId = signupProc.getInt(1);
            return userId + "";
        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(Constants.MSG_FAILED_TO_CREATE_USER);
        }
    }

    private String getErrorResponse(String msg) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(Constants.STATUS_OK);
        errorResponse.setMessage(msg);
        return gson.toJson(errorResponse);
    }

}



