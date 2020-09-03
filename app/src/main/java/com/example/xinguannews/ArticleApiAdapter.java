package com.example.xinguannews;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.net.URL;

public class ArticleApiAdapter {
    final String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    final String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    final String urlParamStrType = "type";
    final String urlParamStrPage = "page";
    final String urlParamStrSize = "size";
    final String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};
    final String defaultType = "all";
    final int defaultPage = 1;
    final int defaultSize = 5;

    /*
    @return all articles
    */
    private ArrayList<Article> getArticleList() {
        return getArticleList(defaultType, defaultPage, defaultSize);
    }

    private ArrayList<Article> getArticleList(String type) {
        return getArticleList(type, defaultPage, defaultSize);
    }

    private ArrayList<Article> getArticleList(String type, int page, int size) {
        String urlStr = getUrlEventList(type, page, size);
        ArrayList<Article> ret = new ArrayList<Article>();
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret;
    }

    private String getUrlEventList(String type, int page, int size) {
        StringBuilder sb = new StringBuilder(urlEventList);
        sb.append("?" + urlParamStrType + "=" + type + "&"
                      + urlParamStrPage + "=" + page + "&"
                      + urlParamStrSize + "=" + size);
        return sb.toString();
    }
}