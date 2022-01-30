package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import org.apache.http.Consts;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RemoteUtils {
    private static final String baseURL = "http://47.94.243.9:11050/";

    public static void post(String url, String body) {
        try {
            HttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .setSocketTimeout(20000)
                    .build();
            HttpPost post = new HttpPost(baseURL + url);
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setConfig(requestConfig);
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
            client.execute(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String url, List<BasicNameValuePair> list, boolean addXiaojiaPrefix) {
        String response = null;
        try {
            HttpClientBuilder client = HttpClients.custom();
            client.addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                if (!request.containsHeader("Pragma"))
                    request.addHeader("Pragma", "no-cache");
                if (!request.containsHeader("Cache-Control"))
                    request.addHeader("Cache-Control", "no-cache");
            });
            client.setUserAgent("XiaojiaAddons/" + XiaojiaAddons.VERSION);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .setSocketTimeout(20000)
                    .build();
            String params = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));

            String fullUrl = url;
            if (params.length() > 0) fullUrl = fullUrl + "?" + params;
            if (addXiaojiaPrefix) fullUrl = baseURL + fullUrl;

            HttpGet request = new HttpGet(fullUrl);
            request.setConfig(requestConfig);
            response = EntityUtils.toString(client.build().execute(request).getEntity(), "UTF-8");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return response;
    }

    public static String get(String url, List<BasicNameValuePair> list) {
        return get(url, list, true);
    }

    public static String get(String url) {
        return get(url, new ArrayList<>());
    }
}
