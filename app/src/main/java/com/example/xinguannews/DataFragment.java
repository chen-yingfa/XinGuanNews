package com.example.xinguannews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xinguannews.api.EpidemicApi;
import com.example.xinguannews.api.EpidemicApiThread;
import com.example.xinguannews.api.EpidemicApiThreadListener;
import com.example.xinguannews.epidemicdata.EpidemicData;

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


    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private int dataType = DEFAULT_DATA;
    private  String message;


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
        this.message = message;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        // TODO: 去掉下一行 (仅为查看是否成功获取 EpidemicData 疫情数据)
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
    }

    // 师叔，你需要获得不同地区疫情数据就调用这个，下载完毕后自动会触发上面的 onFinishGettingEpidemicData()
    public void fetchEpidemicData() {
        EpidemicApi api = new EpidemicApi(getActivity());
        api.addListener(this);
        api.getEpidemicData();
    }
}