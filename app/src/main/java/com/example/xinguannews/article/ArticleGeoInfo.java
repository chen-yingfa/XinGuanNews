package com.example.xinguannews.article;

public class ArticleGeoInfo {
    public String geoName;
    public float latitude;
    public float longitude;
    public String originText;
    public ArticleGeoInfo(String geoName, float latitude, float longitude, String originText) {
        this.geoName = geoName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.originText = originText;
    }
}
