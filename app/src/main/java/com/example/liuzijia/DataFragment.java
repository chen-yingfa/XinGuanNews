package com.example.liuzijia;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liuzijia.api.EpidemicApi;
import com.example.liuzijia.api.EpidemicApiThread;
import com.example.liuzijia.api.EpidemicApiThreadListener;
import com.example.liuzijia.epidemicdata.EpidemicData;
import com.example.liuzijia.epidemicdata.EpidemicDataOneDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataFragment extends Fragment implements EpidemicApiThreadListener {
    private LinearLayout tableCountries;
    private LinearLayout tableProvinces;
    private View view;
    private View loadAnim;
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
        loadAnim = view.findViewById(R.id.load_anim_fragment_data);
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
        genTableData(epidemicDataSet);
        setIsLoading(false);
    }

    @Override
    public void onFetchedEntity(EpidemicApiThread thread) {
    }

    // 师叔，你需要获得不同地区疫情数据就调用这个，下载完毕后自动会触发上面的 onFinishGettingEpidemicData()
    public void fetchEpidemicData() {
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEpidemicData();
    }

    public void genTableData(Set<EpidemicData> epidemicDataSet) {
        genCountryData(epidemicDataSet);
        genProvinceData(epidemicDataSet);
    }

    private void genCountryData(Set<EpidemicData> epidemicDataSet) {
        Map<String, EpidemicData.CountryData> countryMap = new HashMap<>();
        for (EpidemicData elem : epidemicDataSet) {
            EpidemicDataOneDay curData = elem.data.get(elem.data.size() - 1);
            if (elem.province == null) {
                EpidemicData.CountryData countryData = new EpidemicData.CountryData();
                countryData.country = elem.country;
                countryData.initData(curData);
                countryMap.put(elem.country, countryData);
            }
        }
        List<EpidemicData.CountryData> sorted = new ArrayList<>();
        sorted.addAll(countryMap.values());
        Collections.sort(sorted);

        tableCountries = view.findViewById(R.id.epidemic_data_countries);
//        tableCountries.addView(getCountryDataHeaderRow());     // 添加第一行（每列的标题）
        for (EpidemicData.CountryData countryData : sorted) {
            View rowView = countryDataToRow(countryData);
            tableCountries.addView(rowView);
        }
    }

    private void genProvinceData(Set<EpidemicData> epidemicDataSet) {
        Map<String, EpidemicData.CountryData> provinceMap = new HashMap<>();
        for (EpidemicData elem : epidemicDataSet) {
            EpidemicDataOneDay curData = elem.data.get(elem.data.size() - 1);
            if (elem.country.equals("China")) {
                if (elem.county == null) {
                    EpidemicData.CountryData provinceData = new EpidemicData.CountryData();
                    provinceData.country = elem.country;
                    provinceData.province = elem.province;
                    System.out.println(elem.country);
                    provinceData.initData(curData);
                    provinceMap.put(elem.province, provinceData);
                }
            }
        }
        List<EpidemicData.CountryData> sorted = new ArrayList<>();
        sorted.clear();
        sorted.addAll(provinceMap.values());
        Collections.sort(sorted);

        tableProvinces = view.findViewById(R.id.epidemic_data_provinces);
        //        tableCountries.addView(getCountryDataHeaderRow());     // 添加第一行（每列的标题），现在不用了，在 XML 里添加了
        for (EpidemicData.CountryData countryData : sorted) {
            View rowView = provinceDataToRow(countryData);
            tableProvinces.addView(rowView);
        }

        // 将 “国家” 改成 “省份”
        View headerRow = view.findViewById(R.id.province_header_row);
        TextView countryText = headerRow.findViewById(R.id.country_data_row_text_country);
        countryText.setText("省份");
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

    private View provinceDataToRow(EpidemicData.CountryData provinceData) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.country_data_row, null, false);

        TextView viewProvince = view.findViewById(R.id.country_data_row_text_country);
        TextView viewConfirmed = view.findViewById(R.id.country_data_row_text_confirmed);
        TextView viewCured = view.findViewById(R.id.country_data_row_text_cured);
        TextView viewDead = view.findViewById(R.id.country_data_row_text_dead);

        viewProvince.setText(provinceData.province);

        if (provinceData.province == null)
            viewProvince.setText("全国");

        viewConfirmed.setText(provinceData.confirmed.toString());
        viewCured.setText(provinceData.cured.toString());
        viewDead.setText(provinceData.dead.toString());
        return view;
    }

    private View getCountryDataHeaderRow() {
        View view = getRow("国家", "确诊", "治愈", "死亡");
        view.setBackgroundColor(getResources().getColor(R.color.color_primary));
        return view;
    }

    private View getProvinceDataHeaderRow(){
        View view = getRow("省份", "确诊", "治愈", "死亡");
        view.setBackgroundColor(getResources().getColor(R.color.color_primary));
        return view;
    }

    private View getRow(String loc, String confirmed, String cured, String dead){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.country_data_row, null, false);

        TextView viewCountry = view.findViewById(R.id.country_data_row_text_country);
        TextView viewConfirmed = view.findViewById(R.id.country_data_row_text_confirmed);
        TextView viewCured = view.findViewById(R.id.country_data_row_text_cured);
        TextView viewDead = view.findViewById(R.id.country_data_row_text_dead);

        viewCountry.setText(loc);
        viewConfirmed.setText(confirmed);
        viewCured.setText(cured);
        viewDead.setText(dead);
        return view;
    }

    private void setIsLoading(boolean b) {
        if (b) {
            loadAnim.setVisibility(View.VISIBLE);
        } else {
            loadAnim.setVisibility(View.GONE);
        }
    }
}