package team.alpha;

import clover.com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.alpha.model.ErrorResponse;
import team.alpha.model.Event;
import team.alpha.model.WeatherData;
import team.alpha.model.Wind;
import team.alpha.model.owm.OpenWeatherMapData;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@RestController
public class WeatherResource {

    private HttpClient client = new DefaultHttpClient();
    private Gson gson = new Gson();

    @CrossOrigin
    @RequestMapping("/data")
    String getData(
            @RequestParam(value = "lon", defaultValue = "" + Constants.BLOOMINGTON_LON) float lon,
            @RequestParam(value = "lat", defaultValue = "" + Constants.BLOOMINGTON_LAT) float lat) {

        try {
            String strResponse = getDataFromThirdParty(lon, lat);

            if (strResponse == null) {
                return getErrorResponse();
            }

            OpenWeatherMapData openWeatherMapData = gson.fromJson(strResponse, OpenWeatherMapData.class);
            WeatherData weatherData = extractWeatherData(openWeatherMapData);
            return gson.toJson(weatherData);


        } catch (Exception e) {
            e.printStackTrace();
            return getErrorResponse();
        }

    }

    private String getErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.SC_OK);
        errorResponse.setMessage(Constants.ERROR_MSG);
        return gson.toJson(errorResponse);
    }

    String getDataFromThirdParty(float lon, float lat) {

        String strResponse = null;
        try {
            HttpGet request = new HttpGet(Constants.WEATHER_API_URL + "?lat=" + lat + "&lon=" + lon + "&APPID=" + Constants.API_KEY + "&units=imperial");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                strResponse = entity != null ? EntityUtils.toString(entity) : null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strResponse;
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
        event.setSunrise(sunrise.getHour() + ":" + sunrise.getMinute());
        event.setSunset(sunset.getHour() + ":" + sunset.getMinute());
        weatherData.setEvent(event);

        return weatherData;
    }
}

