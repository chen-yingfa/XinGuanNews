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

import com.example.xinguannews.R;
import com.example.xinguannews.article.Article;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleFragment extends Fragment {
    CardView cardViewTemplate;
    LinearLayout linearLayoutCardList;
    LayoutInflater layoutInflater;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public ArticleFragment() {

    }

    public static ArticleFragment newInstance(int index) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void addArticleCard(Article article) {
//        System.out.println("addArticleCard");
        if (layoutInflater == null) {
            System.out.println("Fragment missing: layoutInflater");
            return;
        }
        View cardLayout = articleToCardLayout(article);
        TextView title = (TextView) cardLayout.findViewById(R.id.card_article_title);
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.linearLayoutCardList);
        layout.addView(cardLayout);
    }

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
        return root;
    }
}