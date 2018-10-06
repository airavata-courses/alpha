package team.alpha.model.owm;

import clover.com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    private Float temp;
    @SerializedName("pressure")
    private Integer pressure;
    @SerializedName("humidity")
    private Integer humidity;
    @SerializedName("temp_min")
    private Float tempMin;
    @SerializedName("temp_max")
    private Float tempMax;

    public Float getTemp() {
        return temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

}
