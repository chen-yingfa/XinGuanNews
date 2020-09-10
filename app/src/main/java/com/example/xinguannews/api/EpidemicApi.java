package com.example.xinguannews.api;

import android.app.Activity;


// 外部类应该都通过本接口类调用获取文章信息的函数，此类是 ArticleThread 和其他类的接口
public class EpidemicApi {
    final public static String defaultType = "all";
    final public static int defaultPage = 10;
    final public static int defaultSize = 50;

    private EpidemicApiThreadListener listener;
    private Activity activity;

    public EpidemicApi(Activity activity) {
        this.activity = activity;
    }

    // 添加一个监听器，成功获取 Articles 后通知监听器
    public final void addListener(final EpidemicApiThreadListener listener) {
        this.listener = listener;
    }

    public void getArticles(String type, int page, int size) {
        System.out.println("getArticles: " + type + " " + page + " " + size);
        EpidemicApiThread thr = new EpidemicApiThread(activity, type, page, size);
        thr.setTask("json_articles");
        thr.addListener(listener);
        thr.start();
    }

    public void getEpidemicData() {
        System.out.println("start getting epidemic data...");
        EpidemicApiThread thr = new EpidemicApiThread(activity);
        thr.setTask("epidemicData");
        thr.addListener(listener);
        thr.start();
    }
}