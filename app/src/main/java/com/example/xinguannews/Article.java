package com.example.xinguannews;

import java.util.Date;

enum ArticleType {
    NEWS, PAPER;
}

public class Article {
    private String title;
    private int _id;
    private Date date;
    private String lang;

    public Article(String title, int id, Date date, String lang) {

    }
}
