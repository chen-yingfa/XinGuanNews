package com.example.xinguannews;

import android.widget.Adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URLConnection;
import java.util.ArrayList;
import java.net.URL;

// 用于从新闻接口下载信息
// 注：通过网络下载信息不能阻塞主线程
class AdapterThread extends Thread {
    private final String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    private final String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    private final String urlParamStrType = "type";
    private final String urlParamStrPage = "page";
    private final String urlParamStrSize = "size";
    private final String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};

    private final String defaultType = "all";
    private final int defaultPage = 1;
    private final int defaultSize = 5;

    private ArticleApiAdapter articleApiAdapter;
    private String type;
    private int page;
    private int size;

    // 获取信息的参数通过构造函数传入
    public AdapterThread (ArticleApiAdapter articleApiAdapter, String type, int page, int size) {
        this.articleApiAdapter = articleApiAdapter;
        this.type = type;
        this.page = page;
        this.size = size;
    }

    public void run() {
        String urlStr = getUrlArticles(type, page, size);
        System.out.println("URL: " + urlStr);
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
        ArrayList<Article> articles = parseArticlesJson(stringBuilder.toString());

        // 成功下载并解析新闻信息，返回结果给主线程
        articleApiAdapter.onFinishGettingArticles(articles);
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
    // 返回 Articles 数组（ ArrayList<Article> ）
    private ArrayList<Article> parseArticlesJson(String jsonStr) {
        System.out.println("-----");
        System.out.println("-----");
        System.out.println("parseArticlesJson");
        System.out.println("jsonStr:");
        System.out.println(jsonStr);
        System.out.println("-----");
        System.out.println("-----");
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonStr);
            System.out.println(jsonElement);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray dataArray = jsonObject.get("data").getAsJsonArray();
                for (JsonElement articleElem : dataArray) {
                    JsonObject articleObject = articleElem.getAsJsonObject();
                    System.out.println(articleElem);
                    Article article = parseArticleJson(articleObject);  // 解析文章的 JSON 串
                }
            } else if (jsonElement.isJsonArray()) {

            } else {
                throw new Exception("No valid JSON string found");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // 解析一个 Article 的 JSON 串
    private Article parseArticleJson(JsonObject json) {
        String _id = json.get("_id").getAsString();
        String category = json.get("category").getAsString();
        String content = json.get("content").getAsString();
        String data = json.get("date").getAsString();
        JsonObject articleEntitiesJsonObject = json.get("entities").getAsJsonObject();
        ArrayList<ArticleEntity> articleEntities = parseArticleEntities(articleEntitiesJsonObject);
        String id = json.get("id").getAsString();
        float influence = json.get("influence").getAsFloat();
        String lang = json.get("lang").getAsString();

        return null;
    }

    // JSON 串转成 ArticleEntity 数组
    private ArrayList<ArticleEntity> parseArticleEntities(JsonObject json) {
        ArrayList<ArticleEntity> entities = new ArrayList<ArticleEntity>();
        JsonArray entitiesJsonArray = json.getAsJsonArray();
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
}

public class ArticleApiAdapter extends Thread {
    final String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    final String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    final String urlParamStrType = "type";
    final String urlParamStrPage = "page";
    final String urlParamStrSize = "size";
    final String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};
    final String defaultType = "all";
    final int defaultPage = 1;
    final int defaultSize = 5;

    public void run() {

    }

    /*
    @return all articles
    */
    public void getArticles() {
        getArticles(defaultType, defaultPage, defaultSize);
    }

    public void getArticles(String type) {
        getArticles(type, defaultPage, defaultSize);
    }

    public void getArticles(String type, int page, int size) {
        AdapterThread adapterThread = new AdapterThread(this, type, page, size);
        adapterThread.start();
    }

    public void onFinishGettingArticles(ArrayList<Article> articles) {
        System.out.println("onFinishGettingArticles");
    }

    /*
    * @return url (String) corresponding to the request to call of the API
    * */
    public String getUrlEventList(String type, int page, int size) {
        StringBuilder sb = new StringBuilder(urlEventList);
        sb.append("?" + urlParamStrType + "=" + type + "&"
                      + urlParamStrPage + "=" + page + "&"
                      + urlParamStrSize + "=" + size);
        return sb.toString();
    }
}