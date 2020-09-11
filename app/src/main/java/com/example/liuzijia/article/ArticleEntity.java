package com.example.liuzijia.article;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArticleEntity implements Serializable {
    @SerializedName("label")
    public String label;
    @SerializedName("url")
    public String url;

    public ArticleEntity (String label, String url) {
        this.label = label;
        this.url = url;
    }
}