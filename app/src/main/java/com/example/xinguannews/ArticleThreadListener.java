package com.example.xinguannews;

import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleThread;

public interface ArticleThreadListener {
    void onFinishGettingArticles(final ArticleThread thread);
//    void onLoadPageFinish(final ArticleThread thread);
}
