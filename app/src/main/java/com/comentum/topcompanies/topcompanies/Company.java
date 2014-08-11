package com.comentum.topcompanies.topcompanies;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 8/7/2014.
 */
public class Company {
    public Integer id;
    public String name;
    public String address;
    public String phone;
    public String city;
    public String state;
    public String website;
    public String zip;
    public String description;
    public String description_internal;
    public String description_meta;
    public Integer employees_no;

    public Company() {
        id = 0;
        name = "";
        address = "";
        phone = "";
        city = "";
        state = "";
        website = "";
        zip = "";
        description = "";
        description_internal = "";
        description_meta = "";
        employees_no = 0;
    }

    public void fromJson(JSONObject obj) {
        try {
            if (obj.has("companies_id")) {
                id = obj.getInt("companies_id");
            }
            name = obj.getString("companies_name");
            address = obj.getString("companies_address");
            phone = obj.getString("companies_phone");
            city = obj.getString("companies_city");
            state = obj.getString("companies_state");
            website = obj.getString("companies_website");
            zip = obj.getString("companies_zip");
            if (obj.has("companies_description")) {
                description = obj.getString("companies_description");
            }
            if (obj.has("companies_description_internal")) {
                description_internal = obj.getString("companies_description_internal");
            }
            if (obj.has("companies_description_meta")) {
                description_meta = obj.getString("companies_description_meta");
            }
            if (obj.has("companies_employee_count")) {
                employees_no = obj.getInt("companies_employee_count");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
