package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class UrlEncoderUtil {

    public static String encodeJsonToUrlParameters(JSONObject json) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = null;
            try {
                value = json.get(key).toString();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            result.append(URLEncoder.encode(key, "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(value, "UTF-8"));
            if (keys.hasNext()) {
                result.append("&");
            }
        }
        return result.toString();
    }
}
