package com.example.xinguannews.article;

import java.util.List;

public class Paper extends Article {
    public String aminerId;
    public List<String> authors;
    public String doi;
    public String pdf;

    public Paper (String _id, String category, String content, String date,
                  List<ArticleEntity> entities, List<ArticleGeoInfo> geoInfo, String ID,
                  Float influence, String lang, List<String> regionIds,
                  List<ArticleRelatedEvent> relatedEvents, List<String> segText,
                  String source, Long tFlag, String time, String title, String type,
                  List<String> urls,
                  String aminerId, List<String> authors, String doi, String pdf) {
        super(_id, category, content, date, entities, geoInfo, ID, influence, lang, regionIds,
                relatedEvents, segText, source, tFlag, time, title, type, urls);
        this.aminerId = aminerId;
        this.authors = authors;
        this.doi = doi;
        this.pdf = pdf;
    }

}
