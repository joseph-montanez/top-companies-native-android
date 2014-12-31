package com.comentum.topcompanies.topcompanies.Api;

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

    public Promise detail(String companyId) {
        final DeferredObject deferred = new DeferredObject();

        String apiUrl = "ajax-details.php";
        RequestParams params = new RequestParams();
        params.add("companies_id", companyId);

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
                //{"companies_id":1176,"companies_name":"Agency.com LLC","companies_parent":"","companies_name_url":"agencycom-llc","companies_symbol":"","join_categories_id":46,"join_subcategories_id":207,"join_subsubcategories_id":0,"join_categories":"46,207","companies_price":"0.00","companies_address":"488 Madison Ave., 4th Fl.","companies_city":"New York","companies_state":"NY","companies_zip":"10022","companies_country":"US","companies_website":"http:\/\/www.agency.com","companies_phone":"(212) 358-2600","companies_sales_phone":"","companies_customer_service_phone":"","companies_fax":"(212) 358-2604","companies_founded":0,"companies_employee_count":0,"companies_revenue":0,"companies_accredited":1,"companies_industry":"0","companies_bbb_rating":"0","companies_bbb_accredited":0,"companies_leader_name":"Chan Suh","companies_leader_phone":"","companies_leader_email":"","companies_contact_detail":"","companies_description":"","companies_description_meta":"","companies_description_internal":"Sometimes the name says it all. Agency.com is a leading interactive marketing firm that specializes in creating websites for large corporate clients. Its work includes corporate branding sites, online marketing sites, and e-commerce systems. It also offers media planning and buying services. In addition, Agency.com is active in creating new types of interactive marketing using outdoor displays, mobile phones, and interactive television. Serving such notable clients as 3M, British Airways, Energizer, and Visa, it operates about 10 offices in the US, Europe, and the Asia\/Pacific. Agency.com is a part of the Diversified Agency Services division of advertising and media services giant Omnicom Group.","companies_opinion":"","companies_pros_cons":"","companies_rate_overall":0,"companies_rate_csr":0,"companies_rate_csa":0,"companies_rate_q":0,"companies_rate_pr":0,"companies_size":"large","companies_type":"srv,b2b","companies_date_added":"2013-01-19 16:03:21","companies_live":1,"companies_group_code":"","companies_announcement":"","companies_sales_email":"","companies_support_email":"","companies_strengths":"","testimonials":[],"category":{"categories_id":46,"join_categories_id":0,"categories_name":"Internet and Web","categories_name_url":"internet-and-web","categories_companies_count":87}}
//                List<SearchItem> searchItems = itemsToSearchItems(items);
//                deferred.resolve(searchItems);
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
