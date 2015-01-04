package com.comentum.topcompanies.topcompanies.Api;

import com.comentum.topcompanies.topcompanies.Company;
import com.comentum.topcompanies.topcompanies.SearchItem;
import com.loopj.android.http.RequestParams;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 12/25/2014.
 */
public class Api {
//    enum ERRORS
    public Promise search(String value) {
        final DeferredObject deferred = new DeferredObject();

        String apiUrl = "related-keywords.php";
        RequestParams params = new RequestParams();
        params.add("term", value);

        Promise result = null;
        try {
            result = Client.getJsonDeferred(apiUrl, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result == null) {
            deferred.reject(null);
        }

        result.done(new DoneCallback<JSONArray>() {
            @Override
            public void onDone(JSONArray items) {
                List<SearchItem> searchItems = itemsToSearchItems(items);
                deferred.resolve(searchItems);
            }
        });

        result.fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                deferred.reject(null);
            }
        });

        return deferred.promise();
    }

    public Promise detail(Integer companyId) {
        final DeferredObject deferred = new DeferredObject();

        String apiUrl = "ajax-details.php";
        RequestParams params = new RequestParams();
        params.add("companies_id", String.valueOf(companyId));

        Promise result = null;
        try {
            result = Client.getJsonDeferred(apiUrl, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result == null) {
            deferred.reject(null);
        }

        result.done(new DoneCallback<JSONObject>() {
            @Override
            public void onDone(JSONObject item) {
                Company company = new Company();
                company.fromJson(item);
                deferred.resolve(company);
            }
        });

        result.fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                deferred.reject(null);
            }
        });

        return deferred.promise();
    }

    public Promise listing(String keywordId) {
        final DeferredObject deferred = new DeferredObject();

        String apiUrl = "ajax-listing.php";
        RequestParams params = new RequestParams();
        params.add("keywords_id", keywordId);
        params.add("companies", "true");

        Promise result = null;
        try {
            result = Client.getJsonDeferred(apiUrl, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result == null) {
            deferred.reject(null);
        }

        result.done(new DoneCallback<JSONObject>() {
            @Override
            public void onDone(JSONObject response) {
                if (response.has("companies")) {
                    JSONArray items = response.optJSONArray("companies");
                    if (items != null) {
                        ArrayList<Company> searchItems = itemsToCompanies(items);
                        deferred.resolve(searchItems);
                    } else {
                        deferred.reject(null);
                    }
                } else {
                    deferred.reject(null);
                }
            }
        });

        result.fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                deferred.reject(null);
            }
        });

        return deferred.promise();
    }

    private ArrayList<Company> itemsToCompanies(JSONArray items) {
        ArrayList<Company> companies = new ArrayList<Company>();
        for (int i=0; i < items.length(); i++) {
            Company company = new Company();
            company.fromJson(items.optJSONObject(i));
            companies.add(company);
        }

        return companies;
    }

    private ArrayList<SearchItem> itemsToSearchItems(JSONArray items) {
        ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
        Integer itemsLength = items.length();
        for (Integer i = 0; i < itemsLength; i++) {
            JSONObject item = items.optJSONObject(i);
            if (item != null) {
                String label = item.optString("label");
                String value = item.optString("value");
                try {
                    SearchItem searchItem = new SearchItem(label, value);
                    searchItems.add(searchItem);
                } catch (ExceptionInInitializerError e) {
                    e.printStackTrace();
                }

            }
        }
        return searchItems;
    }
}
