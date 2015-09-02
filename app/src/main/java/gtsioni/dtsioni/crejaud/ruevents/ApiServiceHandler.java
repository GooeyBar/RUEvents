package gtsioni.dtsioni.crejaud.ruevents;


import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ApiServiceHandler
{
    public String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ApiServiceHandler() { }
    public String makeServiceCall(String url, int method, List<NameValuePair> params)
    {
        return this.makeServiceCall(url, method, params, null, null);
    }
    public String makeServiceCall(String url, int method, List<NameValuePair> params, String access_key, String secret_key) {

        if (!isURLValid(url))
        {
            return null;
        }
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            String authorizationString = "";
            boolean hasAccessKey = access_key != null;
            boolean hasSecretKey  = secret_key != null;
            boolean hasKeys = (hasAccessKey && hasSecretKey);
            if (hasKeys)
            {
                String credentials = access_key + ":" + secret_key;
                authorizationString = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            }
            switch (method)
            {
                case GET:
                {
                    HttpGet httpGet = new HttpGet(url);
                    if (hasKeys)
                    {
                        httpGet.addHeader("Accept", "application/json");
                        httpGet.addHeader("Authorization", authorizationString);
                    }
                    httpResponse = httpClient.execute(httpGet);
                }
                break;
                case POST:
                {
                    HttpPost httpPost = new HttpPost(url);
                    if (params != null)
                    {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    }
                    if (hasKeys)
                    {
                        httpPost.setHeader("Accept", "application/json");
                        httpPost.setHeader("Authorization", authorizationString);
                    }
                    httpResponse = httpClient.execute(httpPost);
                }
                break;
            }
            if (httpResponse != null)
            {
                httpEntity = httpResponse.getEntity();
            }
            response = EntityUtils.toString(httpEntity);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
    public String postDailyNote(String url, String params, String access_key, String secret_key) {

        if (!isURLValid(url))
        {
            return null;
        }
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            String authorizationString = "";
            boolean hasAccessKey = access_key != null;
            boolean hasSecretKey  = secret_key != null;
            boolean hasKeys = (hasAccessKey && hasSecretKey);
            if (hasKeys)
            {
                String credentials = access_key + ":" + secret_key;
                authorizationString = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            }
            HttpPost httpPost = new HttpPost(url);
            if (params != null)
            {
                httpPost.setEntity(new StringEntity(params, "UTF-8"));
            }
            if (hasKeys)
            {
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Authorization", authorizationString);
            }
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null)
            {
                httpEntity = httpResponse.getEntity();
            }
            response = EntityUtils.toString(httpEntity);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }
    public Boolean isURLValid(String url)
    {
        try
        {
            URL testForValidity = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}