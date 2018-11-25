package team.alpha;

import clover.com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.ErrorMessage;
import team.alpha.model.Event;
import team.alpha.model.WeatherData;
import team.alpha.model.Wind;
import team.alpha.model.owm.OpenWeatherMapData;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static team.alpha.Constants.*;

@RestController
public class WeatherResource {

    private CloseableHttpClient httpClient;
    private Gson gson = new Gson();
    private static final Map<String, WeatherData> weatherDataMap = new HashMap<>();

    public WeatherResource() throws InterruptedException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
        ScheduledExecutorService dataFetcherService = Executors.newSingleThreadScheduledExecutor();
        dataFetcherService.scheduleAtFixedRate(this::getDataFromThirdParty, 0, 30, TimeUnit.MINUTES);
        Thread.sleep(5000);//initial delay
    }

    @CrossOrigin
    @RequestMapping("/data")
    ResponseEntity getData(
            @RequestParam(value = "city", defaultValue = BLOOMINGTON) String city) {

        try {
            if (!weatherDataMap.containsKey(city)) {
                return new ResponseEntity<>(new ErrorMessage(ERROR_BAD_CITY), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(weatherDataMap.get(city), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void getDataFromThirdParty() {
        for (String city : GPS_LOCATION.keySet()) {
            try {
                HttpGet request = new HttpGet(Constants.WEATHER_API_URL + "?lat=" + Constants.GPS_LOCATION.get(city).getLat() + "&lon=" + Constants.GPS_LOCATION.get(city).getLon() + "&APPID=" + Constants.API_KEY + "&units=imperial");
                request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == HttpStatus.OK.value()) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        OpenWeatherMapData thirdPartyData = gson.fromJson(EntityUtils.toString(entity), OpenWeatherMapData.class);
                        weatherDataMap.put(city, extractWeatherData(thirdPartyData));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private WeatherData extractWeatherData(OpenWeatherMapData owmd) {
        WeatherData weatherData = new WeatherData();

        //set description
        weatherData.setShortDesc(owmd.getWeather().get(0).getMain());
        weatherData.setDescription(owmd.getWeather().get(0).getDescription());

        //set temperature details
        weatherData.setTemperature(Math.round(owmd.getMain().getTemp()));

        //set humidity
        weatherData.setHumidity(owmd.getMain().getHumidity());

        //set wind details
        Wind wind = new Wind();
        wind.setSpeed(owmd.getWind().getSpeed());
        wind.setDegree(owmd.getWind().getDeg());
        weatherData.setWind(wind);

        //set event details
        Event event = new Event();
        LocalTime sunrise = Instant.ofEpochSecond(owmd.getSys().getSunrise()).atZone(ZoneId.systemDefault()).toLocalTime();
        LocalTime sunset = Instant.ofEpochSecond(owmd.getSys().getSunset()).atZone(ZoneId.systemDefault()).toLocalTime();
        event.setSunrise(String.format("%02d", sunrise.getHour()) + ":" + String.format("%02d", sunrise.getMinute()));
        event.setSunset(String.format("%02d", sunset.getHour()) + ":" + String.format("%02d", sunset.getMinute()));
        weatherData.setEvent(event);

        return weatherData;
    }
}

