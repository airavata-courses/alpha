package team.alpha.model.owm;

import clover.com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    private Float lon;
    @SerializedName("lat")
    private Float lat;

    public Float getLon() {
        return lon;
    }

    public Float getLat() {
        return lat;
    }
}
