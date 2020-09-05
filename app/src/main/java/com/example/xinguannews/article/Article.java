package com.example.xinguannews.article;

import java.io.Serializable;
import java.util.List;

public abstract class Article implements Serializable {
    public String _id;
    public String category;
    public String content;
    public String date;
    public List<ArticleEntity> entities;
    public List<ArticleGeoInfo> geoInfo;
    public String ID;
    public Float influence;
    public String lang;
    public List<String> regionIds;
    public List<ArticleRelatedEvent> relatedEvents;
    public List<String> segText;
    public String source;
    public Long tFlag;
    public String time;
    public String title;
    public String type;
    public List<String> urls;

    public Article(String _id, String category, String content, String date,
                   List<ArticleEntity> entities, List<ArticleGeoInfo> geoInfo,
                   String ID, Float influence, String lang, List<String> regionIds,
                   List<ArticleRelatedEvent> relatedEvents, List<String> segText,
                   String source, Long tFlag, String time, String title, String type,
                   List<String> urls) {
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
}


