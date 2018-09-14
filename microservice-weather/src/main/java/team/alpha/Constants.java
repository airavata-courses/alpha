package team.alpha;

public class Constants {

    private Constants() {
        //cant be instantiated
    }

    public static final float BLOOMINGTON_LON = -86.60f;
    public static final float BLOOMINGTON_LAT = 39.17f;
    public static final String API_KEY = System.getenv("WEATHER_API_KEY");
    public static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static final String ERROR_MSG = "Weather Data Not Available";
}
