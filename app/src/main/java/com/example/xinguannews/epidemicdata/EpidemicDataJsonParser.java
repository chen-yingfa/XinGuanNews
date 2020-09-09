package com.example.xinguannews.epidemicdata;

import com.example.xinguannews.JsonParserUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EpidemicDataJsonParser {
    private final String beginName = "begin";
    private final String dataName = "data";

    Set<EpidemicData> epidemicDataSet = new HashSet<>();
    public EpidemicDataJsonParser(JsonObject json) {
        parseJson(json);
    }
    
    public Set<EpidemicData> toEpidemicDataSet() {
        return epidemicDataSet;
    }

    private void parseJson(JsonObject json) {
        Set<Map.Entry<String, JsonElement>> entries = json.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String locStr = entry.getKey();
            String[] locInfo = parseLocation(locStr);
            String country = locInfo[0];
            String province = locInfo[1];
            String county = locInfo[2];

            JsonObject val = entry.getValue().getAsJsonObject();
            String begin = JsonParserUtils.parseString(val, beginName);
            List<EpidemicDataOneDay> data = new ArrayList<>();
            JsonArray arr = val.get(dataName).getAsJsonArray();

            for (JsonElement e : arr) {
                EpidemicDataOneDay oneDay = parseEpidemicDataOneDay(e);
                data.add(oneDay);
            }
            EpidemicData epidemicData = new EpidemicData(country, province, county, begin, data);
            epidemicDataSet.add(epidemicData);
        }
    }

    private EpidemicDataOneDay parseEpidemicDataOneDay(JsonElement json) {
        JsonArray data = json.getAsJsonArray();
        EpidemicDataOneDay epidemicDataOneDay = new EpidemicDataOneDay();
        Integer confirmed = getInt(data.get(0));
        Integer suspected = getInt(data.get(1));
        Integer cured = getInt(data.get(2));
        Integer dead = getInt(data.get(3));
        Integer severe = getInt(data.get(4));
        Integer risk = getInt(data.get(5));
        Integer inc24 = getInt(data.get(6));
        return new EpidemicDataOneDay(confirmed, suspected, cured, dead, severe, risk, inc24);
    }

    // always return String[3]
    private String[] parseLocation(String s) {
        String[] locationInfo = s.split("\\|");
        String country = null;
        String province = null;
        String county = null;
        if (locationInfo.length >= 1) {
            country = locationInfo[0];
        }
        if (locationInfo.length >= 2) {
            province = locationInfo[1];
        }
        if (locationInfo.length >= 3) {
            county = locationInfo[2];
        }
        String[] ret = {country, province, county};
        return ret;
    }

    private Integer getInt(JsonElement json) {
        if (json == null || json.isJsonNull()) return null;
        return json.getAsInt();
    }
}
