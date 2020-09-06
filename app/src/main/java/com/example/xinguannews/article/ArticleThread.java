package com.example.xinguannews.article;

import android.app.Activity;

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
    private final String urlEvent = "https://covid-dashboard.aminer.cn/api/event/";
    private final String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    private final String urlAllEvents = "https://covid-dashboard.aminer.cn/api/dist/events.json";
    private final String urlDiagram = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery";
    private final String urlParamStrType = "type";
    private final String urlParamStrPage = "page";
    private final String urlParamStrSize = "size";
    private final String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};

    private final String defaultType = "all";
    private final int defaultPage = 1;
    private final int defaultSize = 20;

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
    public ArticleThread(Activity activity) {
        this.activity = activity;
        this.type = defaultType;
        this.page = defaultPage;
        this.size = defaultSize;
    }

    // 获取信息的参数通过构造函数传入
    public ArticleThread(Activity activity, String type, int page, int size) {
        this.activity = activity;
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
                    listener.onFinishGettingArticles(thread);
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
        System.out.println("start parsing JSON string of articles...");
        System.out.println("type, page, size = " + type + ", " + page + ", " + size);
        List<Article> articles = new ArrayList<>();
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonStr);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement articleElem : dataArray) {
                    JsonObject articleObj = articleElem.getAsJsonObject();
                    Article article = parseArticle(articleObj);  // 解析文章的 JSON 串
                    articles.add(article);
                }
            } else {
                throw new Exception("No valid JSON string found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Done getting JSON string");
        System.out.println("Got " + articles.size() + " articles");
        for (Article art : articles) {
            System.out.println(art);
        }
        return articles;
    }

    // 解析一个 Article 的 JSON 串
    private Article parseArticle(JsonObject json) {
        ArticleJsonParser parser = new ArticleJsonParser(json);
        return parser.toArticle();
    }
}