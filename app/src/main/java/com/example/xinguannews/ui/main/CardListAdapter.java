package com.example.xinguannews.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xinguannews.R;
import com.example.xinguannews.article.Article;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<Article> articles;

    public CardListAdapter(List<Article> articles) {
//        System.out.println("called constructor of CardListAdapter");
        this.articles = articles;    // 与某个数据进行连接（articles 是个引用）
    }

    // inflate a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View cardViewArticle = inflater.inflate(R.layout.card_article, parent, false);

        // return a holder instance
        return new ViewHolder(cardViewArticle);
    }

    // 给定小标 pos，用对应的 article 的信息给一个 holder 对象赋值
    // 然后 RecyclerView 会自动添加到列表中
    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int pos) {
        Article article = articles.get(pos);

        TextView textTitle = holder.textTitle;
        TextView textContent = holder.textContent;
        TextView textTime = holder.textTime;
        textTitle.setText(article.title);
        textContent.setText(article.content);
        textTime.setText(article.time);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // ViewHolder 会保持 RecyclerView 列表中的元素的 View 的信息
    // 用于动态地对列表元素进行删除和添加
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textContent;
        public TextView textTime;

        public ViewHolder(View itemView) {
            super(itemView);
//            System.out.println("called constructor of ViewHolder");
            textTitle = (TextView) itemView.findViewById(R.id.card_article_title);
            textContent = (TextView) itemView.findViewById(R.id.card_article_content);
            textTime = (TextView) itemView.findViewById(R.id.card_article_time);
        }
    }
}
