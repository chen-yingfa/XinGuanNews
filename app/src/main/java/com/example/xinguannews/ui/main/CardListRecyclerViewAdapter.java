package com.example.xinguannews.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xinguannews.ArticleActivity;
import com.example.xinguannews.R;
import com.example.xinguannews.article.Article;

import java.util.List;

public class CardListRecyclerViewAdapter extends RecyclerView.Adapter<CardListRecyclerViewAdapter.ArticleCardViewHolder> {
    private List<Article> articles;

    public CardListRecyclerViewAdapter(List<Article> articles) {
//        System.out.println("called constructor of CardListAdapter");
        this.articles = articles;    // 与某个数据进行连接（articles 是个引用）
    }

    // inflate a layout from XML and returning the holder
    @Override
    public ArticleCardViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View cardViewArticle = inflater.inflate(R.layout.card_article, parent, false);

        // return a holder instance
        return new ArticleCardViewHolder(cardViewArticle);
    }

    // 给定小标 pos，用对应的 article 的信息给一个 holder 对象赋值
    // 然后 RecyclerView 会自动添加到列表中
    @Override
    public void onBindViewHolder(CardListRecyclerViewAdapter.ArticleCardViewHolder holder, int pos) {
        final int maxLenContent = 64;
        final int maxLenTitle = 42;

        Article article = articles.get(pos);

        TextView textTitle = holder.textTitle;
        TextView textContent = holder.textContent;
        TextView textTime = holder.textTime;
        textTitle.setText(cutIfOverflow(article.title, maxLenTitle));
        textContent.setText(cutIfOverflow(article.content, maxLenContent));
        textTime.setText(article.time);
    }

    public String cutIfOverflow(String s, int maxLen) {
        final String overflowToken = "...";
        if (s.length() > maxLen) {
            return s.substring(0, maxLen - overflowToken.length()) + overflowToken;
        } else {
            return s;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // ViewHolder 会保持 RecyclerView 列表中的元素的 View 的信息
    // 用于动态地对列表元素进行删除和添加
    public class ArticleCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textTitle;
        public TextView textContent;
        public TextView textTime;

        public ArticleCardViewHolder(View itemView) {
            super(itemView);
//            System.out.println("called constructor of ViewHolder");
            textTitle = itemView.findViewById(R.id.card_article_title);
            textContent = itemView.findViewById(R.id.card_article_content);
            textTime = itemView.findViewById(R.id.card_article_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            Article article = articles.get(pos);

            System.out.println("onClick");
            System.out.println(pos);
            System.out.println(article);

            Context context = view.getContext();
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("article", article);
            context.startActivity(intent);
        }
    }
}
