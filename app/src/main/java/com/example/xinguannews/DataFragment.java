package com.example.xinguannews;

import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.epidemicdata.EpidemicData;
import com.example.xinguannews.epidemicdata.EpidemicDataOneDay;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment implements EpidemicApiThreadListener {


    private TableLayout Table;
    private View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
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

    public void setMessage(String message) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_data, container, false);

        // TODO: 去掉下一行 (仅为查看是否成功获取 EpidemicData 疫情数据)
        //    fetchEpidemicData();

        fetchEpidemicData();

      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onFetchedArticles(EpidemicApiThread thread) {
        // EpidemicApiThread 要求重写此函数，
        // 但本 Fragment 不会对成功获取文章数据有反应，所以此处无代码
    }

    // 本函数在成功获取 EpidemicData （地区疫情数据）时会触发
    @Override
    public void onFetchedEpidemicData(EpidemicApiThread thread) {
        System.out.println("onFinishGettingEpidemicData");

        // 师叔，
        // 在此编写下载地区数据完毕后的逻辑
        Set<EpidemicData> epidemicDataSet = thread.getEpidemicDataSet(); // 包含所有地区的疫情数据，是一个 HashSet

        //载入table 数据
        tableDataLoad(epidemicDataSet);
    }

    // 师叔，你需要获得不同地区疫情数据就调用这个，下载完毕后自动会触发上面的 onFinishGettingEpidemicData()
    public void fetchEpidemicData() {
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEpidemicData();
    }

    public void tableDataLoad(Set<EpidemicData> epidemicDataSet)
    {

        Map<String, EpidemicData.covData> dataFilter = new HashMap<>() ;


        int i = 0;
        for(EpidemicData elem:epidemicDataSet)
        {
            if(!dataFilter.containsKey(elem.country))
            {
                EpidemicData.covData dataCollect = new EpidemicData.covData();
                dataCollect.confirmed = elem.data.get(elem.data.size() - 1).confirmed;
                dataCollect.cured= elem.data.get(elem.data.size() - 1).cured;
                dataCollect.dead = elem.data.get(elem.data.size() - 1).dead;

                dataFilter.put(elem.country,dataCollect);
            }
            else
            {
                dataFilter.get(elem.country).confirmed += elem.data.get(elem.data.size() - 1).confirmed;
                dataFilter.get(elem.country).cured += elem.data.get(elem.data.size() - 1).cured;
                dataFilter.get(elem.country).dead += elem.data.get(elem.data.size() - 1).dead;
            }

        }

        Table = view.findViewById(R.id.tab_activity02);
        Table.setStretchAllColumns(true);
        for (Map.Entry<String, EpidemicData.covData> entry : dataFilter.entrySet()){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setBackgroundColor(Color.WHITE);
            for (int j= 0; j<4 ;j++){
                TextView textView = new TextView(getContext());
                if(j == 0)
                textView.setText(entry.getKey());
                if(j == 1)
                    textView.setText(String.valueOf(entry.getValue().cured));
                if(j == 2)
                    textView.setText(String.valueOf(entry.getValue().confirmed));
                if(j == 3)
                    textView.setText(String.valueOf(entry.getValue().dead));


//                textView.setBackground(getResources().getDrawable(R.drawable));
                textView.setGravity(Gravity.LEFT);
                tableRow.addView(textView);
            }
            Table.addView(tableRow,new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT
            ));
        }
    }
}