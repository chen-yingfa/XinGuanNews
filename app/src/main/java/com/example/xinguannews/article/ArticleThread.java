package com.example.xinguannews.article;

import android.app.Activity;

import androidx.appcompat.view.menu.ActionMenuItem;

import com.example.xinguannews.ArticleThreadListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.Arrays;

// 用于从新闻接口下载信息
// 注：通过网络下载信息不能阻塞主线程
public class ArticleThread extends Thread {
    // news API url params
    private final String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    private final String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    private final String urlParamStrType = "type";
    private final String urlParamStrPage = "page";
    private final String urlParamStrSize = "size";
    private final String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};

    private final String defaultType = "all";
    private final int defaultPage = 1;
    private final int defaultSize = 50;

    // json member names
    private final String jsonName_id = "_id";
    private final String jsonNameCategory = "category";
    private final String jsonNameContent = "content";
    private final String jsonNameDate = "date";
    private final String jsonNameEntities = "entities";
    private final String jsonNameGeoInfo = "geoInfo";
    private final String jsonNameID = "id";
    private final String jsonNameInfluence = "influence";
    private final String jsonNameLang = "lang";
    private final String jsonNameRegionIds = "regionIDs";
    private final String jsonNameRelatedEvents = "related_events";
    private final String jsonNameSegText = "seg_text";
    private final String jsonNameSource = "source";
    private final String jsonNameTFlag = "tflag";
    private final String jsonNameTime = "time";
    private final String jsonNameTitle = "title";
    private final String jsonNameType = "type";
    private final String jsonNameUrls = "urls";
    private final String jsonNameAminerId = "aminer_id";
    private final String jsonNameAuthors = "authors";
    private final String jsonNameDoi = "doi";
    private final String jsonNamePdf = "pdf";

    private String type = defaultType;
    private int page = defaultPage;
    private int size = defaultSize;

    private List<ArticleThreadListener> listeners = new ArrayList<>();
    private List<Article> articles = new ArrayList<>();
    private Activity activity;

    // 最小参数的构造函数
    public ArticleThread(Activity activity, List<Article> articles) {
        this.activity = activity;
        this.articles = articles;
        this.type = defaultType;
        this.page = defaultPage;
        this.size = defaultSize;
    }

    // 获取信息的参数通过构造函数传入
    public ArticleThread(Activity activity, List<Article> articles, String type, int page, int size) {
        this.activity = activity;
        this.articles = articles;
        this.type = type;
        this.page = page;
        this.size = size;
    }

    public void addListener(ArticleThreadListener listener) {
        listeners.add(listener);
    }

    public List<ArticleThreadListener> getListeners() {
        return listeners;
    }

    public void run() {
        String urlStr = getUrlArticles(type, page, size);
        System.out.println("API URL: " + urlStr);
        StringBuilder stringBuilder = new StringBuilder(); // 用 StringBuilder 连接每一行
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            // 逐行读入
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        articles = new ArrayList<>();
        articles = parseArticlesJson(stringBuilder.toString());

        // 成功下载并解析新闻信息，返回结果给主线程
        notifyListenersOfFinish(this);
    }

    public void notifyListenersOfFinish(final ArticleThread thread) {
        System.out.println("notifyListenersOfFinish");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ArticleThreadListener listener : listeners) {
                    listener.onThreadFinish(thread);
                }
            }
        });
    }

    public List<Article> getArticles() {
        return articles;
    }

    // 根据参数 type，page，size 生成用于获得 articles 信息（通过新闻接口）的 URL
    private String getUrlArticles(String type, int page, int size) {
        StringBuilder sb = new StringBuilder(urlEventList);
        sb.append("?" + urlParamStrType + "=" + type + "&"
                + urlParamStrPage + "=" + page + "&"
                + urlParamStrSize + "=" + size);
        return sb.toString();
    }

    // 解析新闻接口所返回的 JSON 串
    // 返回 Articles 数组（ List<Article> ）
    private List<Article> parseArticlesJson(String jsonStr) {
//        System.out.println("-----");
        System.out.println("start parsing JSON string of articles...");
//        System.out.println("jsonStr:");
//        System.out.println(jsonStr);
//        System.out.println("-----");
        List<Article> articles = new ArrayList<>();
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonStr);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement articleElem : dataArray) {
                    JsonObject articleObj = articleElem.getAsJsonObject();
//                    System.out.println(articleElem);
                    Article article = parseArticle(articleObj);  // 解析文章的 JSON 串
//                    System.out.println(article);
                    articles.add(article);
                }
            } else {
                throw new Exception("No valid JSON string found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Done getting JSON string");
//        for (Article art : articles) {
//            System.out.println(art);
//        }
        return articles;
    }

    // 解析一个 Article 的 JSON 串
    private Article parseArticle(JsonObject json) {
        // TODO: restructure these parsing functions
        // 因为不同类型的 Article 有一些共同成员，所以为了不用重复代码，
        // 应该在此先解析所有 Article 的共同成员，然后根据 Article 的类
        // 型，给相应的类型的独特成员，可是为了实现动态绑定，我们需要将
        // Article 转换成子类。
        // 注：现在我先直接每个子类自己解析所有成员，这是不好的代码。
        String type = parseString(json, jsonNameType);
//        System.out.println("parseArticle, type: " + type);

        switch(type) {
            case "news":
                return parseNews(json);
            case "paper":
                return parsePaper(json);
            case "event":
                return parseEvent(json);
            default:
                return null;
        }
    }

    private News parseNews(JsonObject json) {
//        System.out.println("parse news");
        System.out.println(json);

        String _id;
        String category;
        String content;
        String date;
        List<ArticleEntity> entities;
        List<ArticleGeoInfo> geoInfos;
        String ID;
        Float influence;
        String lang;
        List<String> regionIds;
        List<ArticleRelatedEvent> relatedEvents;
        List<String> segText;
        String source;
        Long tFlag;
        String time;
        String title;
        String type;
        List<String> urls;

        _id = parseString(json, jsonName_id);
        category = parseString(json, jsonNameCategory);
        content = parseString(json, jsonNameContent);
        date = parseString(json, jsonNameDate);
        entities = parseArticleEntities(json);
        geoInfos = parseGeoInfos(json);
        ID = parseString(json, jsonNameID);
        influence = parseFloat(json, jsonNameInfluence);
        lang = parseString(json, jsonNameLang);
        regionIds = parseRegionIds(json);
        relatedEvents = parseRelatedEvents(json);
        segText = parseSegText(json);
        source = parseSource(json);
        tFlag = parseLong(json, jsonNameTFlag);
        time = parseString(json, jsonNameTime);
        title = parseString(json, jsonNameTitle);
        type = parseString(json, jsonNameType);
        urls = parseUrls(json);
        return new News(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls);
    }

    private Paper parsePaper(JsonObject json) {
        // common member of all Articles
        String _id;
        String category;
        String content;
        String date;
        List<ArticleEntity> entities;
        List<ArticleGeoInfo> geoInfos;
        String ID;
        float influence;
        String lang;
        List<String> regionIds;
        List<ArticleRelatedEvent> relatedEvents;
        List<String> segText;
        String source;
        long tFlag;
        String time;
        String title;
        String type;
        List<String> urls;

        // members of Papers
        String aminerId = parseString(json, jsonNameAminerId);
        List<String> authors = parseAuthors(json);
        String doi = parseString(json, jsonNameDoi);
        String pdf = parseString(json, jsonNamePdf);

        _id = parse_id(json);
        category = parseCategory(json);
        content = parseString(json, jsonNameContent);
        date = parseString(json, jsonNameDate);
        entities = parseArticleEntities(json);
        geoInfos = parseGeoInfos(json);
        ID = parseString(json, jsonNameID);
        influence = parseFloat(json, jsonNameInfluence);
        lang = parseString(json, jsonNameLang);
        regionIds = parseRegionIds(json);
        relatedEvents = parseRelatedEvents(json);
        segText = parseSegText(json);
        source = parseString(json, jsonNameSource);
        tFlag = parseLong(json, jsonNameTFlag);
        time = parseString(json, jsonNameTime);
        title = parseString(json, jsonNameTitle);
        type = parseString(json, jsonNameType);
        urls = parseUrls(json);

        return new Paper(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls,
                aminerId, authors, doi, pdf);
    }

    private Event parseEvent(JsonObject json) {
        // TODO: complete this, need to change Article class accordingly
        String _id;
        String category;
        String content;
        String date;
        List<ArticleEntity> entities;
        List<ArticleGeoInfo> geoInfos;
        String ID;
        float influence;
        String lang;
        List<String> regionIds;
        List<ArticleRelatedEvent> relatedEvents;
        List<String> segText;
        String source;
        long tFlag;
        String time;
        String title;
        String type;
        List<String> urls;

        System.out.println("parseArticleJson");
        _id = parseString(json, jsonName_id);
        category = parseString(json, jsonNameCategory);
        content = parseString(json, jsonNameContent);
        date = parseString(json, jsonNameDate);
        entities = parseArticleEntities(json);
        geoInfos = parseGeoInfos(json);
        ID = parseString(json, jsonNameID);
        influence = parseFloat(json, jsonNameInfluence);
        lang = parseString(json, jsonNameLang);
        regionIds = parseRegionIds(json);
        relatedEvents = parseRelatedEvents(json);
        segText = parseSegText(json);
        source = parseString(json, jsonNameSource);
        tFlag = parseLong(json, jsonNameTFlag);
        time = parseString(json, jsonNameTime);
        title = parseString(json, jsonNameTitle);
        type = parseString(json, jsonNameType);
        urls = parseUrls(json);
        return new Event(_id, category, content, date, entities, geoInfos, ID, influence, lang,
                regionIds, relatedEvents, segText, source, tFlag, time, title, type, urls);
    }

    private String parseType(JsonObject json) {
        return parseString(json, jsonNameType);
    }

    private String parse_id(JsonObject json) {
        return parseString(json, jsonName_id);
    }

    private String parseCategory(JsonObject json) {
        if (json.has(jsonNameCategory)) {
            return json.get(jsonNameCategory).getAsString();
        } else {
            return null;
        }
    }

    // JSON 串转成 ArticleEntity 数组
    private List<ArticleEntity> parseArticleEntities(JsonObject json) {
//        System.out.println("parseArticleEntities");
        if (!json.has(jsonNameEntities)) {
            System.out.println("No entities found");
            return null;
        }
        List<ArticleEntity> entities = new ArrayList<ArticleEntity>();
        JsonArray entitiesJsonArray = json.get(jsonNameEntities).getAsJsonArray();
        for (JsonElement elem : entitiesJsonArray) {
            JsonObject obj = elem.getAsJsonObject();
            ArticleEntity entity = parseArticleEntity(obj);
            entities.add(entity);
        }
        return entities;
    }

    private ArticleEntity parseArticleEntity(JsonObject json) {
        String label = json.get("label").getAsString();
        String url = json.get("url").getAsString();
        return new ArticleEntity(label, url);
    }

    private List<ArticleGeoInfo> parseGeoInfos(JsonObject json) {
//        System.out.println("parseGeoInfos");
        if (!json.has(jsonNameGeoInfo)) {
//            System.out.println("missing GeoInfo member");
            return null;
        }
        JsonArray arr = json.get(jsonNameGeoInfo).getAsJsonArray();
        List<ArticleGeoInfo> list = new ArrayList<ArticleGeoInfo>();
        for (JsonElement e : arr) {
            list.add(parseGeoInfo(e));
        }
        return list;
    }

    private ArticleGeoInfo parseGeoInfo(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String geoName = obj.get("geoName").getAsString();
        float latitude = obj.get("latitude").getAsFloat();
        float longitude = obj.get("longitude").getAsFloat();
        String originText = obj.get("originText").getAsString();
        return new ArticleGeoInfo(geoName, latitude, longitude, originText);
    }

    private List<String> parseRegionIds(JsonObject json) {
//        System.out.println("parseRegionIds");
        if (!json.has(jsonNameRegionIds)) {
//            System.out.println("regionIds missing");
            return null;
        }
        JsonArray arr = json.get(jsonNameRegionIds).getAsJsonArray();
        List<String> list = new ArrayList<String>();
        for (JsonElement e : arr) {
            list.add(e.getAsString());
        }
        return list;
    }

    private List<ArticleRelatedEvent> parseRelatedEvents(JsonObject json) {
        if (!json.has(jsonNameRelatedEvents)) {
//            System.out.println("missing related events");
            return null;
        }
        JsonArray arr = json.get(jsonNameRelatedEvents).getAsJsonArray();
        List<ArticleRelatedEvent> list = new ArrayList<ArticleRelatedEvent>();
        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            String id = obj.get("id").getAsString();
            float score = obj.get("score").getAsFloat();
            list.add(new ArticleRelatedEvent(id, score));
        }
        return list;
    }

    private List<String> parseSegText(JsonElement json) {
        String segTextName = "segText";
        JsonObject obj = json.getAsJsonObject();
        if (!obj.has(segTextName)) {
            return null;
        }
        String s = obj.get(segTextName).getAsString();
        String[] split = s.split(" ");
        return Arrays.asList(split);
    }

    private String parseSource(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        if (obj.has("source")) {
            return obj.get("source").getAsString();
        } else {
            return null;
        }
    }

    private List<String> parseUrls(JsonObject json) {
        if (!json.has(jsonNameUrls)) return null;
        JsonArray arr = json.get(jsonNameUrls).getAsJsonArray();
        List<String> list = new ArrayList<String>();
        for (JsonElement e : arr) {
            list.add(e.getAsString());
        }
        return list;
    }

    private List<String> parseAuthors(JsonObject json) {
        if (!json.has(jsonNameAuthors)) {
            return null;
        }
        List<String> authors = new ArrayList<String>();
        JsonArray arr = json.get(jsonNameAuthors).getAsJsonArray();
        for (JsonElement e : arr) {
            JsonObject obj = e.getAsJsonObject();
            authors.add(obj.get(jsonNameAuthors).getAsString());
        }
        return authors;
    }

    // fundamental parser
    private String parseString(JsonObject json, final String name) {
        if (!json.has(name)) {
//            System.out.println("missing: " + name);
            return null;
        }
        return json.get(name).getAsString();
    }

    private Float parseFloat(JsonObject json, final String name) {
//        System.out.println("parseFloat");
        if (!json.has(name)) {
//            System.out.println("JsonObject missing: " + name);
            return null;
        }
        return json.get(name).getAsFloat();
    }

    private Long parseLong(JsonObject json, final String name) {
        if (!json.has(name)) return null;
        return json.get(name).getAsLong();
    }
}