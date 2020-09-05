package com.example.xinguannews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xinguannews.article.Article;

public class ArticleActivity extends AppCompatActivity {
    TextView textTitle;
    TextView textSource;
    TextView textContent;
    TextView textTime;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        Article article = (Article) getIntent().getSerializableExtra("article");

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
}