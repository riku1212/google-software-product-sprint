package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtility {
    // convert object to json string using gson
    public static <T> String toJson(T obj) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        return jsonString;
    }

    // convert object to json string using gson
    public static <T> String toJsonDisableHtmlEscaping(T obj) {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create(); 
        String jsonString = gson.toJson(obj);
        return jsonString;
    }
}