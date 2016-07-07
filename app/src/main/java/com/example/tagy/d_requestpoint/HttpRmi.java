package com.example.tagy.d_requestpoint;

/**
 * Created by TGy on 11/3/2015.
 */

import android.provider.SyncStateContract;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpRmi {

    private HttpPost request = null;
    private List<NameValuePair> nameValuePairs = null;
    private int connectionTimeoutMillis = 30 * 1000;
    private int socketTimeoutMillis = 0;
    private HttpClient httpClient;

    public HttpRmi(String url) {
        DefaultHttpClient httpclient=new DefaultHttpClient();
        request = new HttpPost(url);
        nameValuePairs = new ArrayList<NameValuePair>(1);
        HttpParams httpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeoutMillis);
        HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUserAgent(httpParams, "Mozilla/5.0 Firefox/26.0");

        httpClient = new DefaultHttpClient(httpParams);
    }

    public HttpRmi add(String key, String value) {
        nameValuePairs.add(new BasicNameValuePair(key, value));
        Log.d("LL","iam in add"+value);
        return this;
    }

    public String execute() throws Exception {
        Log.d("ex","in excute");
        String response = "";
        request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        response = httpClient.execute(request, new BasicResponseHandler());
        return response;

    }
}