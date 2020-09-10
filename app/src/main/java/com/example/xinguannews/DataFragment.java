package com.example.xinguannews;

import android.os.Bundle;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.epidemicdata.EpidemicData;
import com.example.xinguannews.epidemicdata.EpidemicDataOneDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataFragment extends Fragment implements EpidemicApiThreadListener {
    private LinearLayout tableCountries;
    private View view;
    private final String headerProvince = "省份";
    private final String headerCountry = "国家";
    private final String headerConfirmed = "确诊";
    private final String headerCured = "治愈";
    private final String headerDead = "死亡";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_data, container, false);
        fetchEpidemicData();
        return view;
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
        loadTableData(epidemicDataSet);
    }

    // 师叔，你需要获得不同地区疫情数据就调用这个，下载完毕后自动会触发上面的 onFinishGettingEpidemicData()
    public void fetchEpidemicData() {
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEpidemicData();
    }

    public void loadTableData(Set<EpidemicData> epidemicDataSet) {
        Map<String, EpidemicData.CountryData> map = new HashMap<>();
        for (EpidemicData elem : epidemicDataSet) {
            EpidemicDataOneDay curData = elem.data.get(elem.data.size() - 1);
            if (!map.containsKey(elem.country)) {
                EpidemicData.CountryData countryData = new EpidemicData.CountryData();
                countryData.country = elem.country;
                countryData.initData(curData);
                map.put(elem.country, countryData);
            } else {
                map.get(elem.country).addData(curData);
            }
        }

        List<EpidemicData.CountryData> sorted = new ArrayList<>();
        sorted.addAll(map.values());
        Collections.sort(sorted);

        tableCountries = view.findViewById(R.id.epidemic_data_countries);
//        tableCountries.addView(getCountryDataHeaderRow());     // 添加第一行（每列的标题），现在不用了，在 XML 里添加了
        for (EpidemicData.CountryData countryData : sorted) {
            View rowView = countryDataToRow(countryData);
            tableCountries.addView(rowView);
        }
    }
    private View countryDataToRow(EpidemicData.CountryData countryData) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.country_data_row, null, false);
        TextView viewCountry = view.findViewById(R.id.country_data_row_text_country);
        TextView viewConfirmed = view.findViewById(R.id.country_data_row_text_confirmed);
        TextView viewCured = view.findViewById(R.id.country_data_row_text_cured);
        TextView viewDead = view.findViewById(R.id.country_data_row_text_dead);
        viewCountry.setText(countryData.country);
        viewConfirmed.setText(countryData.confirmed.toString());
        viewCured.setText(countryData.cured.toString());
        viewDead.setText(countryData.dead.toString());
        return view;
    }

    private View getCountryDataHeaderRow() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.country_data_row, null, false);
        TextView viewCountry = view.findViewById(R.id.country_data_row_text_country);
        TextView viewConfirmed = view.findViewById(R.id.country_data_row_text_confirmed);
        TextView viewCured = view.findViewById(R.id.country_data_row_text_cured);
        TextView viewDead = view.findViewById(R.id.country_data_row_text_dead);
        viewCountry.setText(headerCountry);
        viewConfirmed.setText(headerConfirmed);
        viewCured.setText(headerCured);
        viewDead.setText(headerDead);
        int textColor = ContextCompat.getColor(getContext(), R.color.colorText);
        viewCountry.setTextColor(textColor);
        viewConfirmed.setTextColor(textColor);
        viewCured.setTextColor(textColor);
        viewDead.setTextColor(textColor);
        return view;
    }
}