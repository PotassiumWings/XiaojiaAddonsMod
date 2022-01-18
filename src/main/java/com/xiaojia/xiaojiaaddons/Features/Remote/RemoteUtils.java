package com.xiaojia.xiaojiaaddons.Features.Remote;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RemoteUtils {
    public static void post(String url) {

    }

    public static String get(String url) {
        String response = null;
        HttpClientBuilder client = HttpClients.custom();
//        client.addInterceptorFirst(
//                (request, context) -> {
//                    if (!request.containsHeader("Pragma"))
//                        request.addHeader("Pragma", "no-cache");
//                    if (!request.containsHeader("Cache-Control"))
//                        request.addHeader("Cache-Control", "no-cache");
//                });
        client.setUserAgent("XiaojiaAddons/" + XiaojiaAddons.VERSION);
        try {
            HttpGet request = new HttpGet(url);
            response = EntityUtils.toString(client.build().execute(request).getEntity(), "UTF-8");
        } catch (Exception exception) {
        }
        return response;
    }
}
