package com.example.liuzijia.article;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArticleGeoInfo implements Serializable {
    @SerializedName("geoName")
    public String geoName;
    @SerializedName("latitude")
    public float latitude;
    @SerializedName("longitude")
    public float longitude;
    @SerializedName("originText")
    public String originText;

    public ArticleGeoInfo(String geoName, float latitude, float longitude, String originText) {
        this.geoName = geoName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.originText = originText;
    }
}
