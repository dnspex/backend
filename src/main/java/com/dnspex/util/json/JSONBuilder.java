package com.dnspex.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JSONBuilder {
    private final Map<String, Object> data = new HashMap<>();

    public static final Gson json = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private JSONBuilder() { }

    public static JSONBuilder create() {
        return new JSONBuilder();
    }

    public JSONBuilder addJson(String key, String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) data.put(key, null);
        else {
            try {
                Map<?, ?> jsonData = json.fromJson(jsonString, Map.class);
                data.put(key, jsonData);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid JSON format for key: " + key, e);
            }
        }
        return this;
    }

    public JSONBuilder add(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public JSONBuilder add(String key, JSONBuilder value) {
        data.put(key, value == null ? null : value.data);
        return this;
    }

    public JSONBuilder addAll(Map<String, Object> map) {
        data.putAll(map);
        return this;
    }


    public JSONBuilder addAll(JSONBuilder jsonBuilder) {
        data.putAll(jsonBuilder.data);
        return this;
    }

    public Map<String, Object> toMap() {
        return new HashMap<>(data);
    }

    public String toJson() {
        return json.toJson(data);
    }
}
