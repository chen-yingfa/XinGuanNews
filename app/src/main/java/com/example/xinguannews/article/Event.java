package com.example.xinguannews.article;

import java.util.List;

public class Event extends Article {
    public Event(String _id, String category, String content, String date,
                 List<ArticleEntity> entities, List<ArticleGeoInfo> geoInfo,
                 String ID, Float influence, String lang, List<String> regionIds,
                 List<ArticleRelatedEvent> relatedEvents, List<String> segText,
                 String source, Long tFlag, String time, String title, String type,
                 List<String> urls) {
        super(_id, category, content, date, entities, geoInfo, ID, influence, lang, regionIds,
                relatedEvents, segText, source, tFlag, time, title, type, urls);
    }
}
