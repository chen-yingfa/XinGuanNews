package com.example.xinguannews.ui.main;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinguannews.R;
import com.example.xinguannews.article.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleFragment extends Fragment {
//    CardView cardViewTemplate;
//    LinearLayout linearLayoutCardList;
    List<Article> articles = new ArrayList<>();
    LayoutInflater layoutInflater;

    // 用于管理 RecyclerView 和其显示数据
    CardListAdapter adapter;
    RecyclerView recyclerView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public ArticleFragment() {
        // pass
    }

    // 添加一个 Article 到列表
    public void addArticleCard(Article article) {
//        System.out.println("addArticleCard");
        articles.add(0, article);
        adapter.notifyItemInserted(0);

//        // 未执行 onCreateView 时，LayoutInflater 为 null，所以无法在列表中添加元素。
//        if (layoutInflater == null) {
//            System.out.println("Fragment missing: layoutInflater");
//            return;
//        }
//        View cardLayout = articleToCardLayout(article);
//        TextView title = (TextView) cardLayout.findViewById(R.id.card_article_title);
//        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linearLayoutCardList);
//        layout.addView(cardLayout);
    }

    // 根据 article 的成员变量返回相应的 CardView（以显示到屏幕上）
    public View articleToCardLayout(Article article) {
        View cardLayout = layoutInflater.inflate(R.layout.card_article, null, true);
        TextView textTitle = (TextView) cardLayout.findViewById(R.id.card_article_title);
        TextView textContent = (TextView) cardLayout.findViewById(R.id.card_article_content);
        TextView textTime = (TextView) cardLayout.findViewById(R.id.card_article_time);
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
        View root = inflater.inflate(R.layout.article_fragment, container, false);
        layoutInflater = inflater;


        // 以下下代码不能放在 onCreate 因为那时候 GetView() 返回 null。
        // （因为此 Fragment 的 View 仍未创建）

        // 与 RecyclerView 及其 Adapter 连接
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_card_list);
        adapter = new CardListAdapter(articles);
        recyclerView.setAdapter(adapter);         // bind RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }
}