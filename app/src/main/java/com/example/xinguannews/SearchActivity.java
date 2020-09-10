package com.example.xinguannews;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.xinguannews.query.QueriesRecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements QueriesRecyclerViewAdapter.ItemClickListener {
    public static List<String> queries = new ArrayList<>();

    // 用于管理 RecyclerView 和其显示数据
    private RecyclerView recyclerView;
    private QueriesRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search);

        // 与 RecyclerView 及其 Adapter 连接
//        recyclerView = findViewById(R.id.search_activity_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup contianer,
//                             Bundle savedInstanceState) {
//
//        return null;
//    }

    public void initAdapter() {
//        adapter = new QueriesRecyclerViewAdapter(queries, this); // bind data (list of json_articles) to Adapter
//        recyclerView.setAdapter(adapter);                   // bind RecyclerView and Adapter
    }

    public void submit(String query) {
        queries.add(query);
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("query", query);
        finish();
    }

    @Override
    public void onItemClick(View view, int pos) {

    }
}