package team.alpha;

import clover.com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import team.alpha.model.UserPreferences;


public class DatabaseResourceTest {

    private DatabaseResource databaseResource;
    private Gson gson = new Gson();

    @Before
    public void setUp() {
        databaseResource = new DatabaseResource();
    }

    @Test
    public void createDBConnection() {
        try {
            databaseResource.createDBConnection();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void login() {
        int userId = databaseResource.login("default", "password");
        Assert.assertEquals(userId, 1000);
    }

    @Test
    public void getUserPref() {
        String userPreferences = databaseResource.getUserPref(1000);
        UserPreferences userPref = gson.fromJson(userPreferences, UserPreferences.class);
        Assert.assertEquals("Bloomington, IN", userPref.getCity());
        Assert.assertEquals("US", userPref.getCountry());
        Assert.assertEquals("Apple", userPref.getCompanies());
    }


}