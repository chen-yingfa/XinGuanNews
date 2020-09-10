package com.example.xinguannews.entity;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

public class Entity {
    public Float hot;
    public String label;
    public String url;
    public String enwiki;
    public String baidu;
    public String zhwiki;
    public Map<String, String> properties;
    public List<EntityRelation> relations;
    public String img;

    public Entity(String hot, String label, String url, String enwiki, String baidu,
                  String zhwiki, Map<String, String> properties, List<EntityRelation> relations,
                  String img) {
        this.properties = properties;
    }

    public Entity(JsonObject json){

    }
}
