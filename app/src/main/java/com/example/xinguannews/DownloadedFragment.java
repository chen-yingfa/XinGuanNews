package com.example.xinguannews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.article.Article;
import com.example.xinguannews.articlelist.CardListRecyclerViewAdapter;
import com.example.xinguannews.epidemicdata.EpidemicData;
import com.example.xinguannews.epidemicdata.EpidemicDataOneDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lecho.lib.hellocharts.model.Line;

public class DownloadedFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager lineatLayoutManager;
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
        lineatLayoutManager = new LinearLayoutManager(getContext());
        context = getContext();
        initAdapter();
        return view;
    }

    private void initAdapter() {
        adapter = new CardListRecyclerViewAdapter(ViewedArticlesManager.viewedArticles, getContext()); // bind data (list of json_articles) to Adapter
        recyclerView.setAdapter(adapter);                   // bind RecyclerView and Adapter
        adapter.notifyDataSetChanged();
    }
}