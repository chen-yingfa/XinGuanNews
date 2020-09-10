package com.example.xinguannews.entity;

import com.example.xinguannews.JsonUtils;
import com.example.xinguannews.epidemicdata.EpidemicData;
import com.example.xinguannews.epidemicdata.EpidemicDataOneDay;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntityJsonParser {
    public Entity entity;
    String nameHot = "hot";
    String nameLabel = "label";
    String nameUrl = "url";
    String nameAbstractInfo = "abstractInfo";
    String nameEnwiki = "enwiki";
    String nameBaidu = "baidu";
    String nameZhwiki = "zhwiki";
    String nameCovid = "COVID";
    String nameProperties = "properties";
    String nameRelations = "relations";
    String nameRelation = "relation";
    String nameForward = "forward";
    String nameImg = "img";

    Set<EpidemicData> epidemicDataSet = new HashSet<>();

    public EntityJsonParser(JsonObject json) {
        parseJson(json);
    }

    public Set<EpidemicData> toEpidemicDataSet() {
        return epidemicDataSet;
    }

    private void parseJson(JsonObject json) {
        entity.hot = JsonUtils.parseFloat(json, nameHot);
        entity.label = JsonUtils.parseString(json, nameLabel);
        entity.url = JsonUtils.parseString(json, nameUrl);

        // abstractInfo
        json = json.get(nameAbstractInfo).getAsJsonObject();
        entity.enwiki = JsonUtils.parseString(json, nameEnwiki);
        entity.baidu = JsonUtils.parseString(json, nameBaidu);
        entity.zhwiki = JsonUtils.parseString(json, nameZhwiki);

        // COVID
        json = json.get(nameCovid).getAsJsonObject();
        JsonObject jsonProperties = json.get(nameProperties).getAsJsonObject();
        entity.properties = new HashMap<String, String>();
        Set<Map.Entry<String, JsonElement>> entries = jsonProperties.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            if (entry.getValue().isJsonNull()) continue;
            String key = entry.getKey();
            String val = entry.getValue().getAsString();
            entity.properties.put(key, val);
        }

        // relations
        EntityRelation rel = new EntityRelation();
        JsonArray relations = json.get(nameRelations).getAsJsonArray();
        for (JsonElement e : relations) {
            json = e.getAsJsonObject();
            rel.relation = JsonUtils.parseString(json, nameRelation);
            rel.url = JsonUtils.parseString(json, nameUrl);
            rel.label = JsonUtils.parseString(json, nameLabel);
            rel.forward = JsonUtils.parseBoolean(json, nameForward);
        }

        entity.img = JsonUtils.parseString(json, nameImg);
    }

    public Entity toEntity() {
        return entity;
    }
}
