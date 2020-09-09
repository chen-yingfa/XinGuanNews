package com.example.xinguannews.article;

import com.example.xinguannews.JsonParserUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArticleJsonParser {
    // json member names
    public final String jsonName_id = "_id";
    public final String jsonNameCategory = "category";
    public final String jsonNameContent = "content";
    public final String jsonNameDate = "date";
    public final String jsonNameEntities = "entities";
    public final String jsonNameGeoInfo = "geoInfo";
    public final String jsonNameID = "id";
    public final String jsonNameInfluence = "influence";
    public final String jsonNameLang = "lang";
    public final String jsonNameRegionIds = "regionIDs";
    public final String jsonNameRelatedEvents = "related_events";
    public final String jsonNameSegText = "seg_text";
    public final String jsonNameSource = "source";
    public final String jsonNameTFlag = "tflag";
    public final String jsonNameTime = "time";
    public final String jsonNameTitle = "title";
    public final String jsonNameType = "type";
    public final String jsonNameUrls = "urls";
    public final String jsonNameAminerId = "aminer_id";
    public final String jsonNameAuthors = "authors";
    public final String jsonNameDoi = "doi";
    public final String jsonNamePdf = "pdf";

    // member names of geoInfo
    public final String jsonNameGeoName = "geoName";
    public final String jsonNameLatitude = "latitude";
    public final String jsonNameLongitude = "longitude";
    public final String jsonNameOriginText = "originText";

    // member names for relatedEvents
    public final String jsonNameRelEventId = "id";
    public final String jsonNameRelEventScore = "score";

    public final String jsonNameAuthorName = "name";

    public JsonObject json;

    // all possible members
    String _id;
    String category;
    String content;
    String date;
    String ID;
    String title;
    String type;
    String lang;
    String time;
    String source;
    String aminerId;
    String doi;
    String pdf;

    Float influence;
    Long tFlag;

    List<String> authors;
    List<String> regionIds;
    List<String> segText;
    List<String> urls;
    List<ArticleEntity> entities;
    List<ArticleGeoInfo> geoInfos;
    List<ArticleRelatedEvent> relatedEvents;

    public ArticleJsonParser(JsonObject json) {
//        System.out.println("instantiates ArticleJsonParser on ");
//        System.out.println(json);
        this.json = json;

        // init all possible members, some of them will be null
        _id = parse_id();
        category = parseCategory();
        content = parseContent();
        date = parseDate();
        ID = parseID();
        type = parseType();
        lang = parseLang();
        title = parseTitle();
        time = parseTime();
        source = parseSource();
        aminerId = parseAminerId();
        doi = parseDoi();
        pdf = parsePdf();

        influence = parseInfluence();
        tFlag = parseTFlag();

        segText = parseSegText();
        regionIds = parseRegionIds();
        urls = parseUrls();
        authors = parseAuthors();
        relatedEvents = parseRelatedEvents();
        entities = parseArticleEntities();
        geoInfos = parseGeoInfos();
    }

    public Article toArticle() {
//        System.out.println("get all values, some of them will be null");
//        System.out.println("type: " + type);
        switch(type) {
            case "news":
//                System.out.println("toNews()");
                return toNews();
            case "paper":
//                System.out.println("toPaper()");
                return toPaper();
            case "event":
//                System.out.println("toEvent()");
                return toEvent();
            default:
                return null;
        }
    }

    public News toNews() {
        return new News(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls);
    }
    public Paper toPaper() {
        return new Paper(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls,
                aminerId, authors, doi, pdf);
    }
    public Event toEvent(){
        return new Event(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls);
    }

    // parse String
    public String parse_id() {
        return parseString(json, jsonName_id);
    }
    public String parseCategory() {
        return parseString(json, jsonNameCategory);
    }
    public String parseContent() {
        return parseString(json, jsonNameContent);
    }
    public String parseDate() {
        return parseString(json, jsonNameDate);
    }
    public String parseID(){
        return parseString(json, jsonNameID);
    }
    public String parseLang() {
        return parseString(json, jsonNameLang);
    }
    public String parseSource(){
        return parseString(json, jsonNameSource);
    }
    public String parseTime(){
        return parseString(json, jsonNameTime);
    }
    public String parseTitle(){
        return parseString(json, jsonNameTitle);
    }
    public String parseType(){
        return parseString(json, jsonNameType);
    }
    public String parseAminerId() {
        return parseString(json, jsonNameAminerId);
    }
    public String parseDoi() {
        return parseString(json, jsonNameDoi);
    }
    public String parsePdf(){
        return parseString(json, jsonNamePdf);
    }

    // parse Long
    public Long parseTFlag(){
        return parseLong(json, jsonNameTFlag);
    }
    // parse Float
    public Float parseInfluence(){
        return parseFloat(json, jsonNameInfluence);
    }

    // parse other data
    public List<ArticleEntity> parseArticleEntities() {
        JsonElement val = json.get(jsonNameEntities);
        if (val == null || val.isJsonNull()) {
            System.out.println("No entities found");
            return null;
        }
        List<ArticleEntity> entities = new ArrayList<ArticleEntity>();
        JsonArray entitiesJsonArray = val.getAsJsonArray();
        for (JsonElement elem : entitiesJsonArray) {
            JsonObject obj = elem.getAsJsonObject();
            ArticleEntity entity = parseArticleEntity(obj);
            entities.add(entity);
        }
        return entities;
    }

    public ArticleEntity parseArticleEntity(JsonElement e) {
        if (e == null || e.isJsonNull()) {
            return null;
        }
        JsonObject json = e.getAsJsonObject();
        String label = json.get("label").getAsString();
        String url = json.get("url").getAsString();
        return new ArticleEntity(label, url);
    }

    public List<ArticleGeoInfo> parseGeoInfos() {
        JsonElement val = json.get(jsonNameGeoInfo);
        if (val == null || val.isJsonNull()) {
//            System.out.println("missing GeoInfo member");
            return null;
        }
        JsonArray arr = val.getAsJsonArray();
        List<ArticleGeoInfo> list = new ArrayList<ArticleGeoInfo>();
        for (JsonElement e : arr) {
            list.add(parseGeoInfo(e));
        }
        return list;
    }

    public ArticleGeoInfo parseGeoInfo(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        JsonObject obj = json.getAsJsonObject();
        String geoName = parseString(obj, jsonNameGeoName);
        Float latitude = parseFloat(obj, jsonNameLatitude);
        Float longitude = parseFloat(obj, jsonNameLongitude);
        String originText = parseString(obj, jsonNameOriginText);
        return new ArticleGeoInfo(geoName, latitude, longitude, originText);
    }

    public List<String> parseRegionIds() {
        JsonElement val = json.get(jsonNameRegionIds);
        if (val == null || val.isJsonNull()) {
            return null;
        }
        JsonArray arr = json.get(jsonNameRegionIds).getAsJsonArray();
        List<String> list = new ArrayList<>();
        for (JsonElement e : arr) {
            list.add(e.getAsString());
        }
        return list;
    }

    public List<ArticleRelatedEvent> parseRelatedEvents() {
        JsonElement val = json.get(jsonNameRelatedEvents);
        if (val == null || val.isJsonNull()) {
//            System.out.println("missing related events");
            return null;
        }
        JsonArray arr = val.getAsJsonArray();
        List<ArticleRelatedEvent> list = new ArrayList<ArticleRelatedEvent>();
        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            list.add(new ArticleRelatedEvent(obj));
        }
        return list;
    }

    public List<String> parseSegText() {
        JsonElement val = json.get(jsonNameSegText);
        if (val == null || val.isJsonNull()) {
            return null;
        }
        String s = val.getAsString();
        return Arrays.asList(s.split(" "));
    }

    public List<String> parseUrls() {
        JsonElement val = json.get(jsonNameUrls);
        if (val == null || val.isJsonNull()) {
            return null;
        }
        JsonArray arr = val.getAsJsonArray();
        List<String> list = new ArrayList<String>();
        for (JsonElement e : arr) {
            list.add(e.getAsString());
        }
        return list;
    }

    public List<String> parseAuthors() {
        JsonElement val = json.get(jsonNameAuthors);
        if (val == null || val.isJsonNull()) {
            return null;
        }
        List<String> authors = new ArrayList<String>();
        JsonArray arr = val.getAsJsonArray();
        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            authors.add(parseString(obj, jsonNameAuthorName));
        }
        return authors;
    }

    // fundamental parser
    public static String parseString(JsonObject json, final String name) {
        return JsonParserUtils.parseString(json, name);
    }
    public static Float parseFloat(JsonObject json, final String name) {
        return JsonParserUtils.parseFloat(json, name);
    }
    public static Long parseLong(JsonObject json, final String name) {
        return JsonParserUtils.parseLong(json, name);
    }
}
