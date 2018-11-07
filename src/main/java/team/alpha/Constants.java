package team.alpha;

import team.alpha.model.GPSLocation;

import java.util.HashMap;
import java.util.Map;

public abstract class Constants {

    public static final Map<String, GPSLocation> GPS_LOCATION = new HashMap<>();

    static {
        //load geo location data
        GPS_LOCATION.put("Bloomington, IN", new GPSLocation(39.167f, -86.53f));
        GPS_LOCATION.put("New York, NY", new GPSLocation(40.67f, -73.96f));
        GPS_LOCATION.put("Los Angeles, CA", new GPSLocation(34.055f, -118.397f));
        GPS_LOCATION.put("Chicago, IL", new GPSLocation(41.875f, -87.655f));
        GPS_LOCATION.put("Houston, TX", new GPSLocation(29.764f, -95.387f));
    }

    private Constants() {
        //cant be instantiated
    }

    public static final String BLOOMINGTON = "Bloomington, IN";
    public static final String API_KEY = System.getenv("WEATHER_API_KEY");
    public static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static final String ERROR_MSG = "Weather Data Not Available";
    public static final String ERROR_FAILED_TO_FETCH = "Weather data not available from third party";
    public static final String ERROR_BAD_CITY = "Weather data for the given city is not available. Make sure you are properly writing the city name";
}
