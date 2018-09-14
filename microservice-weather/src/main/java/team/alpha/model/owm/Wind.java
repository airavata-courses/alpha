package team.alpha.model.owm;

import clover.com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    private Float speed;
    @SerializedName("deg")
    private Integer deg;

    public Float getSpeed() {
        return speed;
    }

    public Integer getDeg() {
        return deg;
    }

}
