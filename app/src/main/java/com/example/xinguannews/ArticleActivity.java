package com.example.xinguannews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xinguannews.article.Article;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.util.List;


public class ArticleActivity extends AppCompatActivity implements WbShareCallback {
    TextView textTitle;
    TextView textSource;
    TextView textContent;
    TextView textTime;
    ImageButton buttonBack;
    ImageButton buttonShare;
    Article article;

    private static final String APP_KY = "3487978721";
    private static final String REDIRECT_URL = "http://open.weibo.com/apps/3487978721/privilege/oauth";
    private static final String SCOPE = "fb67dad57a2c79c91e921ec973268ebc";
    private IWBAPI mWBAPI;

    private void initSdk() {

        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        initSdk();

        article = (Article) getIntent().getSerializableExtra("article");

        buttonBack = findViewById(R.id.article_activity_button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainActivity();
            }
        });

        textTitle = findViewById(R.id.article_activity_text_title);
        textTime = findViewById(R.id.article_activity_text_time);
        textContent = findViewById(R.id.article_activity_text_content);
        textSource = findViewById(R.id.article_activity_text_source);

        setTextViews(article);
        initButtonShare();
        markArticleAsViewed(article);
    }

    private void initButtonShare() {
        buttonShare = findViewById(R.id.article_activity_button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //将要发送到微博得消息 message
                WeiboMultiMessage message = new WeiboMultiMessage();

                TextObject textObject = new TextObject();

                textObject.text = article.title + "\n\n\n" + article.time + "\n" + article.source + "\n\n" + article.content;
                message.textObject = textObject;

                mWBAPI.shareMessage(message, true);
            }
        });
    }

    private void setTextViews(Article article) {
        textTitle.setText(article.title);
        textTime.setText(article.time);
        textContent.setText(article.content);
        textSource.setText(article.source);
    }

    private void backToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void onComplete() {
    }

    @Override
    public void onError(UiError uiError) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWBAPI != null) {
            mWBAPI.doResultIntent(data, new ArticleActivity());
        }
    }

    private void markArticleAsViewed(Article article) {
//        System.out.println("markArticleAsViewed");
//        System.out.println(article);
        ViewedArticlesManager.markArticlesAsViewed(article);
//        System.out.println("num of viewed articles: " + ViewedArticlesManager.viewedArticles.size());
    }
}

