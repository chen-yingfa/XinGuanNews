package com.example.xinguannews;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Article {
    private String _id;
    private String aminer_id;
    private ArrayList<String> authors;
    private String category;
    private String content;
    private String date;
    private String doi;
    private ArrayList<ArticleEntity> entities;
    private ArrayList<String> geoInfo;
    private String id;
    private float influence;
    private String lang;
    private String pdf;
    private ArrayList<String> regionIDs;
    private HashMap<String, Float> relatedEvents;
    private String segText;
    private String source;
    private long tflag;
    private String time;
    private String title;
    private String type;
    private ArrayList<String> urls;
    private int year;

    public Article() {
        authors = new ArrayList<String>();
        relatedEvents = new HashMap<String, Float>();
        entities = new ArrayList<ArticleEntity>();
        geoInfo = new ArrayList<String>();
        regionIDs = new ArrayList<String>();
        urls = new ArrayList<String>();
    }
}


