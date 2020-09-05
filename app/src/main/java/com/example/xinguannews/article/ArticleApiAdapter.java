package com.example.xinguannews.article;

import android.app.Activity;

import com.example.xinguannews.ArticleThreadListener;
import java.util.List;


// 外部类应该都通过本接口类调用获取文章信息的函数，此类是 ArticleThread 和其他类的接口
public class ArticleApiAdapter {
    final public static String urlEventList = "https://covid-dashboard.aminer.cn/api/events/list";
    final public static String urlEpidemicInfo = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    final public static String urlParamStrType = "type";
    final public static String urlParamStrPage = "page";
    final public static String urlParamStrSize = "size";
    final public static String urlParamStrTypeValues[] = {"all", "event", "points", "news", "paper"};
    final public static String defaultType = "all";
    final public static int defaultPage = 10;
    final public static int defaultSize = 50;

    private ArticleThreadListener listener;
    private Activity activity;
    private List<Article> articles;

    public ArticleApiAdapter(Activity activity, List<Article> articles) {
        this.activity = activity;
        this.articles = articles;
    }

    // 添加一个监听器，成功获取 Articles 后通知监听器
    public final void addListener(final ArticleThreadListener listener) {
        this.listener = listener;
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
        ArticleThread adapterThread = new ArticleThread(activity, articles, type, page, size);
        adapterThread.addListener(listener);
        adapterThread.start();
    }

    public void onFinishGettingArticles(final ArticleThread thread) {
        System.out.println("onFinishGettingArticles");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ArticleThreadListener lis : thread.getListeners()) {
                    lis.onThreadFinish(thread);
                }
            }
        });
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