package com.example.xinguannews;

import android.content.Context;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xinguannews.articlelist.CardListRecyclerViewAdapter;

public class DownloadedFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Context context;
    private CardListRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("onCreateView() in DownlaodedFragment");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_downloaded, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_card_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        context = getContext();
        initAdapter();
        return view;
    }

    private void initAdapter() {
        System.out.println("initAdapter");
        System.out.println("num of viewed articles: " + ViewedArticlesManager.viewedArticles.size());
        adapter = new CardListRecyclerViewAdapter(ViewedArticlesManager.viewedArticles, getContext()); // bind data (list of json_articles) to Adapter
        recyclerView.setAdapter(adapter);                   // bind RecyclerView and Adapter
        adapter.notifyDataSetChanged();
    }
}