package com.example.xinguannews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinguannews.article.Article;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;
//import com.sina.weibo.sdk.share.WbShareHandler;


public class ArticleActivity extends AppCompatActivity implements WbShareCallback {
    TextView textTitle;
    TextView textSource;
    TextView textContent;
    TextView textTime;
    ImageButton buttonBack;
    ImageButton buttonShare;

    private static final String APP_KY = "3487978721";
    private static final String REDIRECT_URL = "http://open.weibo.com/apps/3487978721/privilege/oauth";
    private static final String SCOPE = "6a1d8e851c885b417f73ee3ec1998ec9";
    private IWBAPI mWBAPI;

    private void initSdk() {
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    private void startAuth() {
//auth
        mWBAPI.authorize(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(ArticleActivity.this, "􀮙􀜗􀴦􀹦􀱮􀛑", Toast.LENGTH_SHORT);}
            @Override
            public void onError(UiError error) {
                Toast.makeText(ArticleActivity.this, "􀮙􀜗􀴦􀹦􀚊􁲙", Toast.LENGTH_SHORT);}
            @Override
            public void onCancel() {
                Toast.makeText(ArticleActivity.this, "􀮙􀜗􀴦􀹦􀝐􁁾", Toast.LENGTH_SHORT);}
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        initSdk();

        Article article = (Article) getIntent().getSerializableExtra("article");

        buttonBack = findViewById(R.id.article_activity_button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainActivity();
            }
        });

        buttonShare = findViewById(R.id.article_activity_button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "extratext");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ArticleActivity.this.startActivity(Intent.createChooser(intent, "分享"));
  */

                WeiboMultiMessage message = new WeiboMultiMessage();


                TextObject textObject = new TextObject();
                String text = "Try to share";

                textObject.text = text;
                message.textObject = textObject;


                mWBAPI.shareMessage(message, true);




            }
        });

        textTitle = findViewById(R.id.article_activity_text_title);
        textTime = findViewById(R.id.article_activity_text_time);
        textContent = findViewById(R.id.article_activity_text_content);
        textSource = findViewById(R.id.article_activity_text_source);

        setTextViews(article);
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
//        Toast.makeText(this, "成功分享", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError uiError) {
//        Toast.makeText(this, "分享失败" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
//        Toast.makeText(this, "取消分享", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWBAPI != null) {
            mWBAPI.doResultIntent(data, new ArticleActivity());
        }
    }
}

