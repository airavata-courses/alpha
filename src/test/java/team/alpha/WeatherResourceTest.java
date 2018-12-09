package team.alpha;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.spy;
import static team.alpha.Constants.BLOOMINGTON;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherResourceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    private WeatherResource weatherResource;

    @Before
    public void setUp() {
        weatherResource = spy(WeatherResource.class);
    }

    @Test
    public void test2GetData() {
        ResponseEntity response = weatherResource.getData(BLOOMINGTON);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}