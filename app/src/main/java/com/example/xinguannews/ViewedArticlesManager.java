package com.example.xinguannews;

import android.content.Context;
import android.util.Log;

import com.example.xinguannews.article.Article;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewedArticlesManager {
    // key is _id, value if the article
    public static List<Article> viewedArticles = new ArrayList<Article>();
    public static Set<String> viewedIds = new HashSet<>();
    public static final String FILENAME_ARTICLE = "articles.json";
    public static final int bufferSize = 3;   // TODO: make this much larger

    // saves a map of articles in article.json
    public static void saveViewedArticles(Context context) {
        System.out.println("saveArticles, count: " + viewedArticles.size());
        try {
            // 转成 List 后用 Gson 转成 Json 串
            List<Article> articleList = new ArrayList(viewedArticles);
            Gson gson = new Gson();
            String jsonStr = gson.toJson(articleList);
//            System.out.println(jsonStr);
            writeStringToFile(FILENAME_ARTICLE, jsonStr, context);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // loads viewed articles into member variable
    public static void loadViewedArticles(Context context) {
        System.out.println("loadViewedArticles");

        // 读取 JSON 串
        String jsonStr = readStringFromFile(FILENAME_ARTICLE, context);
        if (jsonStr == "") {
            // Failed to read from file;
            viewedArticles = new ArrayList<>();
            return;
        }

        JsonArray jsonArray = JsonParser.parseString(jsonStr).getAsJsonArray();

        // 将读取的 JsonArray 转成 List<Article>
        Type listType = new TypeToken<ArrayList<Article>>() {
        }.getType();
        List<Article> articleList = new Gson().fromJson(jsonArray, listType);

        // 转成 List
        viewedArticles = new ArrayList<>(articleList);
        for (Article a : articleList) {
            viewedIds.add(a._id);
        }

        System.out.println("loaded " + viewedArticles.size() + " articles");
//        for (Article a : viewedArticles) {
//            System.out.println(a);
//        }
    }

    public static void markArticlesAsViewed(Article article) {
        if (!isViewed(article)) {
            addViewedArticle(article);
        }
    }

    public static void addViewedArticle(Article article) {
        if (viewedArticles.size() >= bufferSize) {
            System.out.println("too many viewed articles, remove first one");
            System.out.println(viewedArticles.get(0));

            viewedIds.remove(viewedArticles.get(0)._id);
            viewedArticles.remove(0); // pop
        }
        viewedArticles.add(article);  // push
        viewedIds.add(article._id);
    }

    public static boolean isViewed(Article article) {
        return viewedIds.contains(article._id);
    }

    public static void writeStringToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            osw.write(data);
            osw.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    public static String readStringFromFile(String filename, Context context) {
        String ret = "";
        try {
            FileInputStream fis = context.openFileInput(filename);
            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                ret = builder.toString();
                br.close();
                isr.close();
            }
            fis.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;
    }
}
