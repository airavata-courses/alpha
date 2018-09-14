package team.alpha;

import clover.com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import team.alpha.model.WeatherData;

import java.io.IOException;

public class WeatherResourceTest {

    private WeatherResource weatherResource = new WeatherResource();

    @Test
    public void getData() throws IOException {
        String response = weatherResource.getData(Constants.BLOOMINGTON_LON, Constants.BLOOMINGTON_LAT);
        Assert.assertFalse(response.contains(Constants.ERROR_MSG));
    }
}