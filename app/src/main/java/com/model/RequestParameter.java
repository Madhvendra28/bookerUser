package com.model;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

public class RequestParameter {

    private String uri;
    private String method = "GET";
    private LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    public void setParams(LinkedHashMap<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    public String getParam(String key) {
        return params.get(key).toString();
    }

    public boolean isExist(String key) {
        return params.containsKey(key);
    }

    public String getEncodedParams() {
        try {
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key + "=" + value);
            }
//		Log.d("encoded params", sb.toString());
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
