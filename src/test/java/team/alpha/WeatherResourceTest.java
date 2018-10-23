package team.alpha;

import org.apache.http.HttpStatus;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import team.alpha.model.GPSLocation;
import team.alpha.model.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherResourceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    private WeatherResource weatherResource;
    private static String thirdApiResponse;

    @Before
    public void setUp() {
        weatherResource = spy(WeatherResource.class);
    }

    @Test
    public void test1GetDataFromThirdParty() {
        thirdApiResponse = weatherResource.getDataFromThirdParty(Constants.GPS_LOCATION.get(Constants.BLOOMINGTON));
        Assert.assertNotNull(thirdApiResponse);
    }

    @Test
    public void test2GetData() {
        doReturn(thirdApiResponse).when(weatherResource).getDataFromThirdParty(any(GPSLocation.class));
        Response response = weatherResource.getData(Constants.BLOOMINGTON);
        Assert.assertEquals(response.getStatus(), HttpStatus.SC_OK);
    }
}