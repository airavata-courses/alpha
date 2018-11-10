package team.alpha;

import clover.org.apache.commons.lang.RandomStringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import team.alpha.model.Credentials;
import team.alpha.model.Response;
import team.alpha.model.SignupForm;
import team.alpha.model.UserPreferences;

import static team.alpha.Configurations.ZK_BASEPATH;
import static team.alpha.Configurations.ZK_CONNECTION;
import static team.alpha.model.ResponseStatus.*;

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

        Response response = databaseResource.signup(signupForm);
        Assert.assertEquals(USER_CREATED, response.getStatus());
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
        Assert.assertEquals(USERNAME_CONFLICT, response.getStatus());
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
        Response response = databaseResource.login(credentials);
        Assert.assertEquals(OK, response.getStatus());
    }

    private void checkLoginFails() {
        Assert.assertNotNull(databaseResource);
        Credentials credentials = new Credentials();
        credentials.setUsername(username);
        credentials.setPassword(RandomStringUtils.randomAlphanumeric(16));
        Response response = databaseResource.login(credentials);
        Assert.assertEquals(USER_UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void test4getSubscribedUsersForNews() {
        Response response = databaseResource.getSubscribedUsersForNews();
        Assert.assertEquals(OK, response.getStatus());
    }

    @Test
    public void test5Application() throws Exception {
        App.main(new String[]{});
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(ZK_CONNECTION, new RetryNTimes(5, 1000));
        curatorFramework.start();

        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .basePath(ZK_BASEPATH)
                .client(curatorFramework)
                .build();

        serviceDiscovery.start();
        ServiceProvider dbServiceProvider = serviceDiscovery.serviceProviderBuilder().serviceName("db").build();
        dbServiceProvider.start();

        System.out.println("Attempt three tries to get db service instance from service registry");
        System.out.print("First try - ");
        Thread.sleep(2000);
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");

        Thread.sleep(10000);
        System.out.print("Second try - ");
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");

        Thread.sleep(10000);
        System.out.print("Third try - ");
        Assert.assertNotNull(dbServiceProvider.getInstance());
        System.out.println("SUCCESS");
    }

}