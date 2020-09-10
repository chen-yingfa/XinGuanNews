package com.example.xinguannews.api;

import android.app.Activity;

import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleJson;
import com.example.xinguannews.entity.Entity;
import com.example.xinguannews.entity.EntityJsonParser;
import com.example.xinguannews.epidemicdata.EpidemicData;
import com.example.xinguannews.epidemicdata.EpidemicDataJsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.net.URL;
import java.util.Set;

// 用于从新闻接口下载信息
// 注：通过网络下载信息不能阻塞主线程
public class EpidemicApiThread extends Thread {
    // API url params
    private final String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    private final String urlEvent = "https://covid-dashboard.aminer.cn/api/event/";
    private final String urlEpidemicData = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    private final String urlAllEvents = "https://covid-dashboard.aminer.cn/api/dist/events.json";
    private final String urlEntityQuery = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery";
    private final String urlParamStrType = "type";
    private final String urlParamStrPage = "page";
    private final String urlParamStrSize = "size";

    // NOTE: parameters to set before starting this thread
    private String task;
    private List<EpidemicApiThreadListener> listeners = new ArrayList<>();

    // related to parsing Article
    private final String defaultType = "all";
    private final int defaultPage = 1;
    private final int defaultSize = 20;
    private String type = defaultType;
    private Integer page = defaultPage;
    private Integer size = defaultSize;
    private List<Article> articleList = new ArrayList<>();
    private Activity activity;

    // related to parsing EpidemicData
    private Set<EpidemicData> epidemicDataSet = new HashSet<>();

    // related to parsing Entity
    public Entity entity;
    public String entityQuery;

    // 最少参数的构造函数
    public EpidemicApiThread(Activity activity) {
        this.activity = activity;
    }

    // 获取信息的参数通过构造函数传入，用于获得 Articles
    public EpidemicApiThread(Activity activity, String type, int page, int size) {
        this.activity = activity;
        setArticleParams(type, page, size);
    }

    public void addListener(EpidemicApiThreadListener listener) {
        listeners.add(listener);
    }
    /**************************************************
    *                     SETTERS
    * *************************************************/
    public void setTask(String task) { this.task = task; }
    public void setArticleParams(String type, int page, int size) {
        this.type = type;
        this.page = page;
        this.size = size;
    }
    public void setEntityParams(String entityQuery) {
        this.entityQuery = entityQuery;
    }

    /**************************************************
    *                     GETTERS
    * *************************************************/
    public String getTask() { return task; }

    public List<EpidemicApiThreadListener> getListeners() {
        return listeners;
    }
    // getters to use by listeners when finished
    public List<Article> getArticleList() { return articleList; }
    public Set<EpidemicData> getEpidemicDataSet() { return epidemicDataSet; }

    public void run() {
        switch (task) {
            case "json_articles":
                if (type == null || page == null || size == null) {
                    System.out.println("ERROR: did not set parameter before fetching json_articles");
                    return;
                }
                fetchArticles();
                break;
            case "epidemicData":
                fetchEpidemicData();
                break;
            case "entity":
                fetchEntity();
                break;
            default:
                System.out.println("ERROR: did not set task before running apiThread");
        }
    }

    public void notifyListenersOfFinish(final EpidemicApiThread thread) {
        System.out.println("notifyListenersOfFinish");
        switch(task) {
            case "json_articles":
                notifyListenersOfFinishGettingArticles(thread);
            case "epidemicData":
                notifyListenersOfFinishGettingEpidemicData(thread);
            case "entity":
                notifyListenersOfFinishFetchingEntity(thread);
        }
    }

    public void notifyListenersOfFinishGettingArticles(final EpidemicApiThread thread) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (EpidemicApiThreadListener listener : listeners) {
                    listener.onFetchedArticles(thread);
                }
            }
        });
    }

    public void notifyListenersOfFinishGettingEpidemicData(final EpidemicApiThread thread) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (EpidemicApiThreadListener listener : listeners) {
                    listener.onFetchedEpidemicData(thread);
                }
            }
        });
    }

    public void notifyListenersOfFinishFetchingEntity(final EpidemicApiThread thread) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (EpidemicApiThreadListener listener : listeners) {
                    listener.onFetchedEntity(thread);
                }
            }
        });
    }

    public String getStringFromUrl(String urlStr) {
        System.out.println("Getting from URL: " + urlStr);
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
        return stringBuilder.toString();
    }

    public void fetchArticles() {
        String urlStr = getUrlArticles(type, page, size);
        String jsonStr = getStringFromUrl(urlStr);
        articleList = new ArrayList<>();
        articleList = parseArticlesJson(jsonStr);

        // 成功下载并解析新闻信息，返回结果给主线程
        notifyListenersOfFinish(this);
    }

    // returns set of epidemic data ( Set<EpidemicData> )
    public void fetchEpidemicData() {
        String jsonStr = getStringFromUrl(urlEpidemicData);

        // turn string into JsonObject and parse using EpidemicDataJsonParser, then notify listeners
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        EpidemicDataJsonParser parser = new EpidemicDataJsonParser(jsonObject);
        epidemicDataSet = parser.toEpidemicDataSet();
        notifyListenersOfFinish(this);
    }


    public void fetchEntity() {
        String url = urlEntityQuery + "?entity=" + entityQuery;
        String jsonStr = getStringFromUrl(url);
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        JsonElement data = jsonObject.get("data");
        if (data == null || data.isJsonNull()) {
            notifyListenersOfFinish(this);
        }
        EntityJsonParser entityJsonParser = new EntityJsonParser(data.getAsJsonObject());
        entity = entityJsonParser.toEntity();
        notifyListenersOfFinish(this);
    }

    // 根据参数 type，page，size 生成用于获得 json_articles 信息（通过新闻接口）的 URL
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
        System.out.println("start parsing JSON string of json_articles...");
        System.out.println("type, page, size = " + type + ", " + page + ", " + size);
        List<Article> articles = new ArrayList<>();
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonStr);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray arr = jsonObject.get("data").getAsJsonArray();
                for (JsonElement e : arr) {
                    articles.add(parseArticle(e));  // 解析文章的 JSON 串
                }
            } else {
                throw new Exception("No valid JSON string found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Done getting JSON string");
        System.out.println("Got " + articles.size() + " json_articles");
//        for (Article art : json_articles) {
//            System.out.println(art);
//        }
        return articles;
    }

    // 解析一个 Article 的 JSON 串
    private Article parseArticle(JsonElement json) {
        ArticleJson parser = new ArticleJson(json.getAsJsonObject());
        return parser.toArticle();
    }
}