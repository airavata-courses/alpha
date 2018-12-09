package team.alpha;

import clover.org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.ResponseEntity;
import team.alpha.model.Credentials;
import team.alpha.model.SignupForm;
import team.alpha.model.UserPreferences;

import static org.springframework.http.HttpStatus.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseResourceTest {

    private static DatabaseResource databaseResource;
    private static String username = RandomStringUtils.randomAlphabetic(10) + "@domain.com";
    private static String password = RandomStringUtils.randomAlphanumeric(16);
    private static final String city = "Bloomington, IN";
    private static final String country = "US";
    private static final String company = "Apple";
    private static final boolean subscribedToNewsAlerts = false;
    private static final boolean subscribedToWeatherAlerts = false;

    @Test
    public void test1createDBConnection() {
        try {
            databaseResource = new DatabaseResource();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void test2SignupTests() {
        checkSignupSucceeds();
        checkSignupFails();
    }

    private void checkSignupSucceeds() {
        Assert.assertNotNull(databaseResource);
        SignupForm signupForm = new SignupForm();
        signupForm.setCredentials(new Credentials());
        signupForm.getCredentials().setUsername(username);
        signupForm.getCredentials().setPassword(password);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setCity(city);
        userPreferences.setCountry(country);
        userPreferences.setCompany(company);
        userPreferences.setSubscribedToNewsAlerts(subscribedToNewsAlerts);
        userPreferences.setSubscribedToWeatherAlerts(subscribedToWeatherAlerts);
        signupForm.setUserPreferences(userPreferences);

        ResponseEntity response = databaseResource.signup(signupForm);
        Assert.assertEquals(CREATED, response.getStatusCode());
    }

    private void checkSignupFails() {
        Assert.assertNotNull(databaseResource);
        SignupForm signupForm = new SignupForm();
        signupForm.setCredentials(new Credentials());
        signupForm.getCredentials().setUsername(username);
        signupForm.getCredentials().setPassword(password);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setCity(city);
        userPreferences.setCountry(country);
        userPreferences.setCompany(company);
        userPreferences.setSubscribedToNewsAlerts(subscribedToNewsAlerts);
        userPreferences.setSubscribedToWeatherAlerts(subscribedToWeatherAlerts);
        signupForm.setUserPreferences(userPreferences);

        ResponseEntity response = databaseResource.signup(signupForm);
        Assert.assertEquals(CONFLICT, response.getStatusCode());
    }

    @Test
    public void test3LoginTests() {
        checkLoginSucceeds();
        checkLoginFails();
    }

    private void checkLoginSucceeds() {
        Assert.assertNotNull(databaseResource);
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        ResponseEntity response = databaseResource.login(credentials);
        Assert.assertEquals(OK, response.getStatusCode());
    }

    private void checkLoginFails() {
        Assert.assertNotNull(databaseResource);
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(RandomStringUtils.randomAlphanumeric(16));
        ResponseEntity response = databaseResource.login(credentials);
        Assert.assertEquals(UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void test4getSubscribedUsersForNews() {
        ResponseEntity response = databaseResource.getSubscribedUsersForNews();
        Assert.assertEquals(OK, response.getStatusCode());
    }
}