package team.alpha;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherResourceTest {

    private WeatherResource weatherResource;
    private static String thirdApiResponse;

    @Before
    public void setUp() {
        weatherResource = spy(WeatherResource.class);
    }

    @Test
    public void test1GetDataFromThirdParty() {
        thirdApiResponse = weatherResource.getDataFromThirdParty(Constants.BLOOMINGTON_LON, Constants.BLOOMINGTON_LAT);
        Assert.assertNotNull(thirdApiResponse);
    }

    @Test
    public void test2GetData() {
        doReturn(thirdApiResponse).when(weatherResource).getDataFromThirdParty(anyFloat(), anyFloat());
        String response = weatherResource.getData(Constants.BLOOMINGTON_LON, Constants.BLOOMINGTON_LAT);
        Assert.assertFalse(response.contains(Constants.ERROR_MSG));
    }

}