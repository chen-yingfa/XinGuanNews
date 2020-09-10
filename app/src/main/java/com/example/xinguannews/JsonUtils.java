package com.example.xinguannews;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {
    // fundamental parser
    public static String parseString(JsonObject json, final String name) {
        JsonElement val = json.get(name);
        if (val == null || val.isJsonNull()) {
            System.out.println("parseString(), missing: " + name);
            return null;
        }
        return val.getAsString();
    }
    public static Float parseFloat(JsonObject json, final String name) {
        JsonElement val = json.get(name);
        if (val == null || val.isJsonNull()) {
            System.out.println("ParseFloat(), missing: " + name);
            return null;
        }
        return val.getAsFloat();
    }
    public static Long parseLong(JsonObject json, final String name) {
        JsonElement val = json.get(name);
        if (val == null || val.isJsonNull()) {
            System.out.println("parseLong(), missing: " + name);
            return null;
        }
        return val.getAsLong();
    }
}
