package com.comentum.topcompanies.topcompanies;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 8/7/2014.
 */
public class Company {
    public String name;
    public String address;
    public String phone;
    public String city;
    public String state;
    public String website;
    public String zip;

    public Company() {
        name = "";
        address = "";
        phone = "";
        city = "";
        state = "";
        website = "";
        zip = "";
    }

    public void fromJson(JSONObject obj) {
        try {
            name = obj.getString("companies_name");
            address = obj.getString("companies_address");
            phone = obj.getString("companies_phone");
            city = obj.getString("companies_city");
            state = obj.getString("companies_state");
            website = obj.getString("companies_website");
            zip = obj.getString("companies_zip");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
