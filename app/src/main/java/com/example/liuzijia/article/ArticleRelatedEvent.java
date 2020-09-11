package com.example.liuzijia.article;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArticleRelatedEvent implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("score")
    public float score;
    public String jsonNameRelEventId = "id";
    public String jsonNameRelEventScore = "score";

    public ArticleRelatedEvent(String id, float score) {
        this.id = id;
        this.score = score;
    }

    public ArticleRelatedEvent(JsonObject json) {
        String id = ArticleJson.parseString(json, jsonNameRelEventId);
        Float score = ArticleJson.parseFloat(json, jsonNameRelEventScore);
    }
}
