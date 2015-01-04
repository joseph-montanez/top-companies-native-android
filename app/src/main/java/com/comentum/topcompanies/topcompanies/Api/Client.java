package com.comentum.topcompanies.topcompanies.Api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 12/25/2014.
 */
public class Client {
    private static final String BASE_URL = "http://hawk2.comentum.com/topcompanies/app-api/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpClient getClient() {
        client.setTimeout(60000);
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
        return client;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static Promise getJsonDeferred(String url, RequestParams params) throws JSONException {
        final Deferred deferred = new DeferredObject();
        String finalUrl = getAbsoluteUrl(url);
        Log.d("ApiClient", "url:" + finalUrl);
        AsyncHttpClient client = getClient();
        client.cancelAllRequests(true);
        client.get(finalUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Client", "onSuccess - JSONObject");
                if (response != null) {
                    Log.d("JSONObject", response.toString());
                }


                deferred.resolve(response);
//                // If the response is JSONObject instead of expected JSONArray
//                boolean success = response.optBoolean("success", false);
//                if (success) {
//                    deferred.resolve(response);
//                } else {
//                    deferred.reject(null);
//                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("Client", "onSuccess - JSONArray");
                deferred.resolve(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                deferred.reject(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.Throwable throwable, JSONArray errorResponse) {
                deferred.reject(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                deferred.reject(null);
            }
        });

        return deferred.promise();
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}