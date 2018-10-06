package team.alpha;

import clover.com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.ErrorResponse;
import team.alpha.model.UserPreferences;

import java.sql.*;
import java.util.Properties;

@RestController
class DatabaseResource {

    private Gson gson = new Gson();
    private Connection db;

    DatabaseResource() {
        try {
            createDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createDBConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        db = DriverManager.getConnection(Constants.DB_URL, properties);
    }

    @CrossOrigin
    @RequestMapping("/login")
    int login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        int userId = -1;

        try {
            PreparedStatement st = db.prepareStatement("select userid from userinfo where username = ? and password = ?");
            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                userId = rs.getInt(1);
            }

            if (userId == -1) {
                System.out.println(Constants.MSG_INVALID_CREDENTIAL);
            }

            rs.close();
            st.close();
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
            PreparedStatement st = db.prepareStatement("select city, country, companies from userpref where userid = ?");
            st.setInt(1, userId);

            ResultSet rs = st.executeQuery();

            UserPreferences userPreferences = null;
            while (rs.next()) {
                userPreferences = new UserPreferences();
                userPreferences.setCity(rs.getString(1));
                userPreferences.setCountry(rs.getString(2));
                userPreferences.setCompanies(rs.getString(3));
            }

            rs.close();
            st.close();

            if (userPreferences == null) {
                return getErrorResponse(Constants.MSG_USER_PREF_NOT_AVAILABLE);
            }

            return gson.toJson(userPreferences);

        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(Constants.MSG_FAILED_TO_FETCH_USER_PREF);
        }

    }

    private String getErrorResponse(String msg) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(Constants.STATUS_OK);
        errorResponse.setMessage(msg);
        return gson.toJson(errorResponse);
    }

}



