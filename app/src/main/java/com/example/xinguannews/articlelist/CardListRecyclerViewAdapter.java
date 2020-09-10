package com.example.xinguannews.articlelist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinguannews.ArticleActivity;
import com.example.xinguannews.R;
import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleJson;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

// 管理新闻列表中的卡片的 Adapter
public class CardListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    final int maxLenContent = 64;
    final int maxLenTitle = 42;
    public boolean clickable = true;

    public List<Article> articles;

    public CardListRecyclerViewAdapter(List<Article> articles) {
        this.articles = articles;
    }

    // inflate a layout from XML, and return a corresponding holder instance;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View view = inflater.inflate(R.layout.card_article, parent, false);
            return new ArticleViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.anim_load_more, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    // 给定小标 pos，用对应的 article 的信息给一个 holder 对象赋值
    // 然后 RecyclerView 会自动添加到列表中
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        final int maxLenContent = 64;
        final int maxLenTitle = 42;
        if (holder instanceof ArticleViewHolder) {
            setListItem((ArticleViewHolder) holder, pos);
        } else {
            showLoadingView((LoadingViewHolder) holder, pos);
        }
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
        return articles == null ? 0 : articles.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return articles.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    // ViewHolder 会保持 RecyclerView 列表中的元素的 View 的信息
    // 用于动态地对列表元素进行删除和添加
    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textTitle;
        public TextView textContent;
        public TextView textTime;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
//            System.out.println("called constructor of ViewHolder");
            textTitle = itemView.findViewById(R.id.card_article_title);
            textContent = itemView.findViewById(R.id.card_article_content);
            textTime = itemView.findViewById(R.id.card_article_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickable == false) {
                return;
            }
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

    // Loading view, at the end of the list
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder holder, int pos) {

    }

    private void setListItem(ArticleViewHolder holder, int pos) {
        Article article = articles.get(pos);

        TextView textTitle = holder.textTitle;
        TextView textContent = holder.textContent;
        TextView textTime = holder.textTime;
        textTitle.setText(cutIfOverflow(article.title, maxLenTitle));
        textContent.setText(cutIfOverflow(article.content, maxLenContent));
        textTime.setText(article.time);
    }
}
