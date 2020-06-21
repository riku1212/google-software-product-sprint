package com.google.sps.servlets;

import com.google.gson.Gson;

public class JsonUtility {
    // convert object to json string using gson
    public static <T> String toJson(T obj) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        return jsonString;
    }
}