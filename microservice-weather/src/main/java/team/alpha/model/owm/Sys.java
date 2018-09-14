package team.alpha.model.owm;

import clover.com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("type")
    private Integer type;
    @SerializedName("id")
    private Integer id;
    @SerializedName("message")
    private Float message;
    @SerializedName("country")
    private String country;
    @SerializedName("sunrise")
    private Long sunrise;
    @SerializedName("sunset")
    private Long sunset;

    public Integer getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public Float getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

}
