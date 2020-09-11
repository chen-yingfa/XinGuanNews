package com.example.xinguannews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.entity.Entity;
import com.example.xinguannews.entity.EntityRelation;
import com.example.xinguannews.entitylist.EntityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GraphSchemaFragment extends Fragment implements EpidemicApiThreadListener {

    private SearchView searchView;
    private LinearLayout resultContainer;
    private View view;
    private String query;
    public List<Entity> entities = new ArrayList<>();
    RecyclerView recyclerView;
    public Boolean isLoading;
    View loadingAnim;
    LinearLayoutManager linearLayoutManager;
    EntityRecyclerViewAdapter adapter;

    public GraphSchemaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graph_schema, container, false);
        searchView = view.findViewById(R.id.mSearch);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query) {
                fetchEntity(query);
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        loadingAnim = view.findViewById(R.id.activity_graph_schema_load_anim);
        recyclerView = view.findViewById(R.id.graph_schema_result_container);
        setIsLoading(false);
        initRecyclerView();
        return view;
    }

    public void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        adapter = new EntityRecyclerViewAdapter(entities, getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFetchedArticles(EpidemicApiThread thread) {}

    @Override
    public void onFetchedEpidemicData(EpidemicApiThread thread) {}

    @Override
    public void onFetchedEntity(EpidemicApiThread thread) {
        // 师叔，在这里先写获得 Entity 数据后的逻辑
//        List<Entity> entities = thread.getEntities();
//        resultContainer = view.findViewById(R.id.graph_schema_result_container);
//        for (Entity entity : entities) {
//            View entityRow = genDataRowOfEntity(entity);
//            resultContainer.addView(entityRow);
//        }
        adapter.collapseAll();
//        entities.clear();
//        for (Entity entity : thread.getEntities()) {
//            adapter.addEntity(entity, EntityRecyclerViewAdapter.VIEW_TYPE_COLLAPSED);
//        }
        setIsLoading(false);
        adapter.setEntities(thread.entities);
        adapter.notifyDataSetChanged();
    }

    public void fetchEntity(String entity) {
        setIsLoading(true);
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEntity(entity);
    }

    public View genDataRowOfEntity(Entity entity) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.entity_data_row, null, false);
        TextView textLabel = view.findViewById(R.id.text_entity_label);
        textLabel.setText(entity.label);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Entity entity =
//                Context context = view.getContext();
//                Intent intent = new Intent(context, EntityActivity.class);
//                intent.putExtra("entity", entity);
//                context.startActivity(intent);
            }
        });
        return view;
    }

    public void setIsLoading(Boolean b) {
        if (b == true) {
            loadingAnim.setVisibility(View.VISIBLE);
        } else {
            loadingAnim.setVisibility(View.GONE);
        }
        isLoading = b;
    }

}