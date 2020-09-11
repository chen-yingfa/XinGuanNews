package com.example.xinguannews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.entity.Entity;
import com.example.xinguannews.epidemicdata.EpidemicData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphSchemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphSchemaFragment extends Fragment implements EpidemicApiThreadListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView mSearchView;

    private LinearLayout  tableEntity;
    private LinearLayout  tableANangel;
    private LinearLayout  table

    private String searchEntity;


    public GraphSchemaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphSchemaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphSchemaFragment newInstance(String param1, String param2) {
        GraphSchemaFragment fragment = new GraphSchemaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_schema, container, false);
        mSearchView = view.findViewById(R.id.mSearch);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return view;


    }

    @Override
    public void onFetchedArticles(EpidemicApiThread thread) {}

    @Override
    public void onFetchedEpidemicData(EpidemicApiThread thread) {}

    @Override
    public void onFetchedEntity(EpidemicApiThread thread) {
        // 师叔，在这里先写获得 Entity 数据后的逻辑
        List<Entity> entity = thread.getEntities();
        TextView viewEntity = getView().findViewById(R.id.text_entity);
        TextView viewBaidu = getView().findViewById(R.id.text_description);

        viewEntity.setText(entity.label);
        viewBaidu.setText(entity.baidu);

    }

    public void fetchEntity(String entity) {
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEntity(entity);
    }

}