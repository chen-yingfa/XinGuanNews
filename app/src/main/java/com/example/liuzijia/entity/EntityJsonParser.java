package com.example.liuzijia.entity;

import com.example.liuzijia.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityJsonParser {
    public Entity entity = new Entity();
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

    public EntityJsonParser(JsonObject json) {
        parseJson(json);
    }

    private void parseJson(JsonObject json) {
        entity.hot = JsonUtils.parseFloat(json, nameHot);
        entity.label = JsonUtils.parseString(json, nameLabel);
        entity.url = JsonUtils.parseString(json, nameUrl);
        entity.img = JsonUtils.parseString(json, nameImg);

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
        entity.relations = new ArrayList<>();
        JsonArray relations = json.get(nameRelations).getAsJsonArray();
        for (JsonElement e : relations) {
            JsonObject obj = e.getAsJsonObject();
            EntityRelation rel = new EntityRelation();
            rel.relation = JsonUtils.parseString(obj, nameRelation);
            rel.url = JsonUtils.parseString(obj, nameUrl);
            rel.label = JsonUtils.parseString(obj, nameLabel);
            rel.forward = JsonUtils.parseBoolean(obj, nameForward);
            entity.relations.add(rel);
        }
    }

    public Entity toEntity() {
        return entity;
    }
}
