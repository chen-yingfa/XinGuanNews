package com.example.xinguannews.epidemicdata;

import java.util.List;

public class EpidemicData {
    public String country;
    public String province;
    public String county;
    public String begin;
    public List<EpidemicDataOneDay> data;

    public EpidemicData() {
    }

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

    public static class CountryData implements Comparable<CountryData> {
        public String country;
        public Integer confirmed;
        public Integer cured;
        public Integer dead;

        public CountryData() {
        }

        public void addData(CountryData other) {
            confirmed += other.confirmed;
            cured += other.cured;
            dead += other.dead;
        }

        public void addData(EpidemicDataOneDay other) {
            confirmed += other.confirmed;
            cured += other.cured;
            dead += other.dead;
        }

        public void initData(EpidemicDataOneDay other) {
            confirmed = other.confirmed;
            cured = other.cured;
            dead = other.dead;
        }

        @Override
        public int compareTo(CountryData other) {
            if (confirmed == other.confirmed) {
                if (dead == other.dead) {
                    if (cured == other.cured) {
                        return other.country.compareTo(country);
                    }
                    return other.cured.compareTo(cured);
                }
                return other.dead.compareTo(dead);
            }
            return other.confirmed.compareTo(confirmed);
        }
    }


}

