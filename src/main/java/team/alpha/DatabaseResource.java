package team.alpha;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.Credentials;
import team.alpha.model.NewsSubscribers;
import team.alpha.model.SignupForm;
import team.alpha.model.UserPreferences;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static team.alpha.Configurations.DB_URL;
import static team.alpha.Constants.*;

@RestController
class DatabaseResource {
    private Connection db;
    private CallableStatement loginProc;
    private CallableStatement signupProc;
    private PreparedStatement newsSubscribersStmt;

    DatabaseResource() {
        createDBConnection();
        createPreparedStatements();
    }

    private void createDBConnection() {
        try {
            db = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a database connection");
        }
    }

    private void createPreparedStatements() {
        try {
            loginProc = db.prepareCall("{ call login_user(?,?) }");
            signupProc = db.prepareCall("{ ? = call create_user(?,?,?,?,?,?,?) }");
            signupProc.registerOutParameter(1, Types.INTEGER);
            newsSubscribersStmt = db.prepareStatement("select p.country, array_to_json(array_agg(i.username)) from\n" +
                    "(select userid, country from userpref where get_news_alerts = true) p\n" +
                    "inner join userinfo i on i.userid = p.userid\n" +
                    "group by p.country;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping("/login")
    ResponseEntity<?> login(@RequestBody Credentials credentials) {
        int userId = -1;
        UserPreferences userPreferences = new UserPreferences();

        int retries = 0;
        while (retries < 2) {
            try {
                loginProc.setString(1, credentials.getUsername());
                loginProc.setString(2, credentials.getPassword());

                ResultSet rs = loginProc.executeQuery();

                while (rs.next()) {
                    userId = rs.getInt(1);
                    if (userId > 0) {
                        userPreferences.setCity(rs.getString(2));
                        userPreferences.setCountry(rs.getString(3));
                        userPreferences.setCompany(rs.getString(4));
                        userPreferences.setSubscribedToNewsAlerts(rs.getBoolean(5));
                        userPreferences.setSubscribedToWeatherAlerts(rs.getBoolean(6));
                    }
                }

                if (userId == -1) {
                    return new ResponseEntity<>(MSG_INVALID_CREDENTIALS, UNAUTHORIZED);
                }

                return new ResponseEntity<>(userPreferences, OK);

            } catch (SQLException e) {
                e.printStackTrace();
                createDBConnection();
                createPreparedStatements();
            }
            retries++;
        }

        return new ResponseEntity<>(MSG_FAILED_TO_FETCH_USER, INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin
    @RequestMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupForm signupForm) {
        int retries = 0;
        while (retries < 2) {
            try {
                signupProc.setString(2, signupForm.getCredentials().getUsername());
                signupProc.setString(3, signupForm.getCredentials().getPassword());
                UserPreferences userPreferences = signupForm.getUserPreferences();
                signupProc.setString(4, userPreferences.getCity());
                signupProc.setString(5, userPreferences.getCountry());
                signupProc.setString(6, userPreferences.getCompany());
                signupProc.setBoolean(7, userPreferences.isSubscribedToNewsAlerts());
                signupProc.setBoolean(8, userPreferences.isSubscribedToWeatherAlerts());

                signupProc.execute();

                int userId = signupProc.getInt(1);

                if (userId == 0) {
                    return new ResponseEntity<>(MSG_USER_ALREADY_EXISTS, CONFLICT);
                }

                return new ResponseEntity<>(userId, CREATED);

            } catch (SQLException e) {
                e.printStackTrace();
                createDBConnection();
                createPreparedStatements();
            }
            retries++;
        }

        return new ResponseEntity<>(MSG_FAILED_TO_CREATE_USER, INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin
    @RequestMapping("/newssubscribers")
    ResponseEntity<?> getSubscribedUsersForNews() {

        int retries = 0;

        while (retries < 2) {
            List<NewsSubscribers> newsSubscribersList = new ArrayList<>();
            try {
                ResultSet rs = newsSubscribersStmt.executeQuery();

                while (rs.next()) {
                    NewsSubscribers newsSubscribers = new NewsSubscribers();
                    newsSubscribers.setCountry(rs.getString(1));
                    newsSubscribers.setUsernames(rs.getString(2));
                    newsSubscribersList.add(newsSubscribers);
                }

                return new ResponseEntity<>(newsSubscribersList, OK);

            } catch (SQLException e) {
                e.printStackTrace();
                createDBConnection();
                createPreparedStatements();
            }
            retries++;
        }

        return new ResponseEntity<>(MSG_FAILED_TO_FETCH_SUBSCRIBER_LIST, OK);
    }
}
