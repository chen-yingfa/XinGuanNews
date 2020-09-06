package com.example.xinguannews.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.xinguannews.R;
import com.example.xinguannews.ArticleThreadListener;
import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleApiAdapter;
import com.example.xinguannews.article.ArticleThread;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ArticleThreadListener {
//    CardView cardViewTemplate;
//    LinearLayout linearLayoutCardList;

    private String type;
    private List<Article> articles = new ArrayList<>();
    private LayoutInflater layoutInflater;

    // 用于管理 RecyclerView 和其显示数据
    private CardListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public CardListFragment(String type) {
        this.type = type;
    }

    // 添加一个 Article 到列表
    public void addArticleCard(Article article) {
//        System.out.println("addArticleCard");
        articles.add(0, article);
        adapter.notifyItemInserted(0);
    }

    // 根据 article 的成员变量返回相应的 CardView（以显示到屏幕上）
    public View articleToCardLayout(Article article) {
        View cardLayout = layoutInflater.inflate(R.layout.card_article, null, true);
        TextView textTitle = cardLayout.findViewById(R.id.card_article_title);
        TextView textContent = cardLayout.findViewById(R.id.card_article_content);
        TextView textTime = cardLayout.findViewById(R.id.card_article_time);
        textTitle.setText(article.title);
        textContent.setText(article.content);
        textTime.setText(article.date);
        return cardLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_list_fragment, container, false);
        layoutInflater = inflater;


        // 以下下代码不能放在 onCreate 因为那时候 GetView() 返回 null。
        // （因为此 Fragment 的 View 仍未创建）

        // 与 RecyclerView 及其 Adapter 连接
        recyclerView = root.findViewById(R.id.recycler_view_card_list);
        adapter = new CardListRecyclerViewAdapter(articles);
        recyclerView.setAdapter(adapter);            // bind RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // listen to refresh gesture (swipe down)
        swipeRefreshLayout = root.findViewById(R.id.swiperefresh_article);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
        return root;
    }

    public void updateArticles(List<Article> articles) {
        this.articles = articles;
        adapter = new CardListRecyclerViewAdapter(articles);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // 在下载线程下载完毕时候运行（该线程将下载的文章数据传过来）
    @Override
    public void onThreadFinish(ArticleThread thread) {
        System.out.println("onThreadFinish");
        updateArticles(thread.getArticles());
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        System.out.println("onRefresh");
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        Activity act = getActivity();
        System.out.println(act);
        if (act != null) {
            ArticleApiAdapter articleApiAdapter = new ArticleApiAdapter(act, articles);
            articleApiAdapter.addListener(this);
            articleApiAdapter.getArticles(type);
        }
    }
}