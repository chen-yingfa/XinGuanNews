package com.example.xinguannews.articlelist;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.xinguannews.R;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.article.Article;
import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;

import java.util.ArrayList;
import java.util.List;

/**
*   一个由卡片组成的列表的 fragment，用于显示新闻（文章）列表
* */
public class CardListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, EpidemicApiThreadListener {
    private int nPage = 1;
    private int pageSize = 20;
    private int maxLoadedArticlesCount = 1000;

    public String type;
    public String title;
    public String filter = "";
    private List<Article> articles = new ArrayList<>();
    private List<Article> articlesFiltered = new ArrayList<>();
    boolean isLoading = false;

    // 用于管理 RecyclerView 和其显示数据
    private RecyclerView recyclerView;
    private CardListRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CardListFragment(String type, String title) {
        this.type = type;
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 注：此函数末端会调用 onRefresh，即所有 CardListFragment 都会在创建是自动刷新获取内容。
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.card_list_fragment, container, false);

        // 以下下代码不能放在 onCreate 因为那时候 GetView() 返回 null。
        // （因为此 Fragment 的 View 仍未创建）

        // 与 RecyclerView 及其 Adapter 连接
        recyclerView = root.findViewById(R.id.recycler_view_card_list);
        swipeRefreshLayout = root.findViewById(R.id.swiperefresh_article);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        initAdapter();
        initRefreshListener();
        initScrollListener();
        onRefresh();
        return root;
    }

    private void initAdapter() {
        adapter = new CardListRecyclerViewAdapter(articlesFiltered); // bind data (list of articles) to Adapter
        recyclerView.setAdapter(adapter);                   // bind RecyclerView and Adapter
    }

    private void initRefreshListener() {
        // listen to refresh gesture (swipe down)
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && articles.size() < maxLoadedArticlesCount) {
                    if (llm != null && llm.findLastVisibleItemPosition() == articlesFiltered.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
        articlesFiltered.addAll(filterArticles(articles, filter));
        adapter.notifyDataSetChanged();
    }

    public void clearArticles() {
        articles.clear();
        articlesFiltered.clear();
    }

    public List<Article> filterArticles(List<Article> articles, String filter) {
        filter = filter.toLowerCase();
        System.out.println("filterArticles with filter: " + filter + " in articles cnt: " + articles.size());
        List<Article> filtered = new ArrayList<>();
        for (Article a : articles) {
            String title = a.title.toLowerCase();
            if (title.contains(filter)) {
                filtered.add(a);
            }
        }
        System.out.println(filtered.size());
        return filtered;
    }

    public void updateFilter(String filter) {
        this.filter = filter;
        pageSize = 20 + filter.length() * 10;
        articlesFiltered.clear();
        articlesFiltered.addAll(filterArticles(articles, filter));
        adapter.notifyDataSetChanged();
    }

    // 在下载线程下载完毕时候运行（该线程将下载的文章数据传过来）
    @Override
    public void onFetchedArticles(EpidemicApiThread thread) {
        System.out.println("onThreadFinish");
        if (swipeRefreshLayout.isRefreshing()) { // refresh
            clearArticles();
            swipeRefreshLayout.setRefreshing(false); // make sure refresh animation stops
            nPage = 0;
        }
        ++nPage;
        // load more
        addArticles(thread.getArticleList());
        isLoading = false;
    }

    @Override
    public void onFetchedEpidemicData(EpidemicApiThread thread) {
        // this Fragment do not react to getting epidemic data
    }

    @Override
    public void onRefresh() {
        System.out.println("onRefresh, type: " + type);
        // TODO: don't refresh if just recently refreshed
        // make sure the refreshing animation starts
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        nPage = 1;
        Activity act = getActivity();
        System.out.println(act);
        if (act != null) {
            fetctArticles(type, nPage, pageSize);
            isLoading = false;
        }
    }

    // load more articles and append to end of list
    public void loadMore() {
        System.out.println("loadMore");
        // NOTE: must not block UI thread
        articlesFiltered.add(null);
        adapter.notifyItemInserted(articlesFiltered.size() - 1);

        class LoadMoreRunnable implements Runnable {
            EpidemicApiThreadListener listener;
            public LoadMoreRunnable(EpidemicApiThreadListener listener) {
                this.listener = listener;
            }
            @Override
            public void run() {
                if (articlesFiltered.size() > 0){
                    articlesFiltered.remove(articlesFiltered.size() - 1);
                    int scrollPosition = articlesFiltered.size();
                    adapter.notifyItemRemoved(scrollPosition);
                }
                fetctArticles(type, nPage + 1, pageSize);
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new LoadMoreRunnable(this), 1200);
        if (!isLoading) {
            loadMoreOnNeed();
        }
    }

    public void loadMoreOnNeed() {
        LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (!isLoading && articles.size() < maxLoadedArticlesCount) {
            if (llm != null && llm.findLastVisibleItemPosition() == articlesFiltered.size() - 1) {
                loadMore();
                isLoading = true;
            }
        }
    }

    public void fetctArticles(String type, int page, int size) {
        EpidemicApi api = new EpidemicApi(getActivity());

        api.addListener(this);
        api.getArticles(type, nPage, pageSize);
    }

    public String getType() {
        return type;
    }

    public void search(String query) {
        System.out.println("onSearchTextChanged");
        updateFilter(query);
        loadMoreOnNeed();
    }
}