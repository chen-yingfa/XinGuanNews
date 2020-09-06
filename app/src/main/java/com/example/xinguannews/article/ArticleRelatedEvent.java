package com.example.xinguannews.article;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class ArticleRelatedEvent implements Serializable {
    public String id;
    public float score;
    public String jsonNameRelEventId = "id";
    public String jsonNameRelEventScore = "score";

    public ArticleRelatedEvent(String id, float score) {
        this.id = id;
        this.score = score;
    }

    public ArticleRelatedEvent(JsonObject json) {
        String id = ArticleJsonParser.parseString(json, jsonNameRelEventId);
        Float score = ArticleJsonParser.parseFloat(json, jsonNameRelEventScore);
    }
}
