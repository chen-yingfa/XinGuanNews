package com.example.xinguannews.epidemicdata;

import java.util.List;

public class EpidemicData {
    String country;
    String province;
    String county;
    String begin;
    List<EpidemicDataOneDay> data;

    public EpidemicData() {}

    public EpidemicData(String country, String province, String county,
                        String begin, List<EpidemicDataOneDay> data) {
        this.country = country;
        this.province = province;
        this.county = county;
        this.begin = begin;
        this.data = data;
    }

    public String toString() {
        return country + " " + province + " " + county + " " + begin + " " + data.get(0);
    }
}
