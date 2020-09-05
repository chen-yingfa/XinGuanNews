package com.example.xinguannews;

import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleThread;

public interface ArticleThreadListener {
    void onThreadFinish(final ArticleThread thread);
}
