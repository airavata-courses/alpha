package team.alpha;

import clover.com.google.gson.Gson;
import clover.org.apache.commons.lang.RandomStringUtils;
import clover.org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import team.alpha.model.UserPreferences;
import team.alpha.model.UserSignupForm;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseResourceTest {

    private DatabaseResource databaseResource;
    private Gson gson = new Gson();
    private String username;
    private String password;
    private final String city = "Bloomington, IN";
    private final String country = "US";
    private final String company = "Apple";
    private final boolean subscribedToNewsAlerts = false;
    private final boolean subscribedToWeatherAlerts = true;

    @Test
    public void test1createDBConnection() {
        try {
            DatabaseResource.createDBConnection();
            databaseResource = new DatabaseResource();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test2DatabaseResource() {
        databaseResource = new DatabaseResource();
        setRandomValues();
        int userId = signup();
        login(userId);

    }

    private void login(int expectedUserId) {
        Assert.assertNotNull(databaseResource);
        int userId = databaseResource.login(username, password);
        Assert.assertEquals(expectedUserId, userId);
    }

    private void setRandomValues() {
        username = RandomStringUtils.randomAlphabetic(16);
        password = RandomStringUtils.randomAlphanumeric(16);
    }

    private int signup() {
        Assert.assertNotNull(databaseResource);
        UserSignupForm userSignupForm = new UserSignupForm();
        userSignupForm.setUsername(username);
        userSignupForm.setPassword(password);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setCity(city);
        userPreferences.setCountry(country);
        userPreferences.setCompany(company);
        userPreferences.setSubscribedToNewsAlerts(subscribedToNewsAlerts);
        userPreferences.setSubscribedToWeatherAlerts(subscribedToWeatherAlerts);
        userSignupForm.setUserPreferences(userPreferences);

        String userId = databaseResource.signup(gson.toJson(userSignupForm));
        Assert.assertFalse(userId.equalsIgnoreCase(Constants.MSG_FAILED_TO_CREATE_USER));
        return Integer.parseInt(userId);
    }

    @Override
    public String toString() {
        return "DatabaseResourceTest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", subscribedToNewsAlerts=" + subscribedToNewsAlerts +
                ", subscribedToWeatherAlerts=" + subscribedToWeatherAlerts +
                '}';
    }
}