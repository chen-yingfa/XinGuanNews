package com.example.xinguannews.article;

import java.io.Serializable;

public class ArticleEntity implements Serializable {
    public String label;
    public String url;

    public ArticleEntity (String label, String url) {
        this.label = label;
        this.url = url;
    }
}