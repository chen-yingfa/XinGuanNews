package com.example.xinguannews.article;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    @SerializedName("_id")
    public String _id;
    @SerializedName("category")
    public String category;
    @SerializedName("content")
    public String content;
    @SerializedName("date")
    public String date;
    @SerializedName("entities")
    public List<ArticleEntity> entities;
    @SerializedName("geoInfo")
    public List<ArticleGeoInfo> geoInfo;
    @SerializedName("id")
    public String ID;
    @SerializedName("influence")
    public Float influence;
    @SerializedName("lang")
    public String lang;
    @SerializedName("regionIDs")
    public List<String> regionIds;
    @SerializedName("relatedEvents")
    public List<ArticleRelatedEvent> relatedEvents;
    @SerializedName("seg_text")
    public List<String> segText;
    @SerializedName("source")
    public String source;
    @SerializedName("tflag")
    public Long tFlag;
    @SerializedName("time")
    public String time;
    @SerializedName("title")
    public String title;
    @SerializedName("type")
    public String type;
    @SerializedName("urls")
    public List<String> urls;
    @SerializedName("aminer_id")
    public String aminerId;
    @SerializedName("authors")
    public List<String> authors;
    @SerializedName("doi")
    public String doi;
    @SerializedName("pdf")
    public String pdf;

    public Article(String _id, String category, String content, String date,
                   List<ArticleEntity> entities, List<ArticleGeoInfo> geoInfo,
                   String ID, Float influence, String lang, List<String> regionIds,
                   List<ArticleRelatedEvent> relatedEvents, List<String> segText,
                   String source, Long tFlag, String time, String title, String type,
                   List<String> urls, String aminerId, List<String> authors, String doi,
                   String pdf) {
        this._id = _id;
        this.category = category;
        this.content = content;
        this.date = date;
        this.entities = entities;
        this.geoInfo = geoInfo;
        this.ID = ID;
        this.influence = influence;
        this.lang = lang;
        this.regionIds = regionIds;
        this.relatedEvents = relatedEvents;
        this.segText = segText;
        this.source = source;
        this.tFlag = tFlag;
        this.time = time;
        this.title = title;
        this.type = type;
        this.urls = urls;
        this.aminerId = aminerId;
        this.authors = authors;
        this.doi = doi;
        this.pdf = pdf;
    }

    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("_id: " + _id + "\n"
//                + "title: " + title + "\n"
//                + "category: " + category + "\n"
//                + "content: " + content + "\n"
//                + "date: " + date + "\n"
//                + "type: " + type
//        );
//        return sb.toString();
        return type + " " + title;
    }

    public JsonObject toJsonObject() {
        
        return null;
    }
}


