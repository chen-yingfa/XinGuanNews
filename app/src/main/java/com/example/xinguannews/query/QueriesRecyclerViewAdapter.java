package com.example.xinguannews.query;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xinguannews.HomeFragment;
import com.example.xinguannews.R;
import com.example.xinguannews.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class QueriesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<String> queries;
    LayoutInflater layoutInflater;
    ItemClickListener itemClickListener;

    public QueriesRecyclerViewAdapter(List<String> queries, ItemClickListener itemClickListener, Context context) {
        this.queries = queries;
        this.itemClickListener = itemClickListener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_history_item, parent, false);
        return new QueryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setListItem((QueryViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return queries.size();
    }

    public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        public final TextView textView;

        public QueryViewHolder(View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.text_search_list_item);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("onClick");
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int pos);
    }

    private void setListItem(QueriesRecyclerViewAdapter.QueryViewHolder holder, int pos) {
        String query = queries.get(pos);
        TextView textView = holder.textView;
        textView.setText(query);
    }
}