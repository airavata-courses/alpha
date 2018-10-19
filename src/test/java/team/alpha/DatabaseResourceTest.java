package team.alpha;

import clover.org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import team.alpha.model.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseResourceTest {

    private DatabaseResource databaseResource;
    private String username = RandomStringUtils.randomAlphabetic(16);
    private String password= RandomStringUtils.randomAlphanumeric(16);
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
        checkSignupSucceeds();
        checkSignupFails();
        checkLoginSucceeds();
        checkLoginFails();
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

        Response response = databaseResource.signup(signupForm);
        Assert.assertEquals(response.getStatus(), ResponseStatus.USER_CREATED);
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

        Response response = databaseResource.signup(signupForm);
        Assert.assertEquals(response.getStatus(), ResponseStatus.USERNAME_CONFLICT);
    }

    private void checkLoginSucceeds() {
        Assert.assertNotNull(databaseResource);
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        Response response = databaseResource.login(credentials);
        Assert.assertEquals(response.getStatus(), ResponseStatus.OK);
    }

    private void checkLoginFails() {
        Assert.assertNotNull(databaseResource);
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(RandomStringUtils.randomAlphanumeric(16));
        Response response = databaseResource.login(credentials);
        Assert.assertEquals(response.getStatus(), ResponseStatus.USER_UNAUTHORIZED);
    }

}