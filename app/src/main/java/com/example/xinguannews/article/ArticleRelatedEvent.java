package com.example.xinguannews.article;

import java.io.Serializable;

public class ArticleRelatedEvent implements Serializable {
    public String id;
    public float score;
    public ArticleRelatedEvent(String id, float score) {
        this.id = id;
        this.score = score;
    }
}
