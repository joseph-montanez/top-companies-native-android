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
    private Integer revenue;
    private Integer founded;

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
        /*
        {
          "companies_id": 1176,
          "companies_name": "Agency.com LLC",
          "companies_parent": "",
          "companies_name_url": "agencycom-llc",
          "companies_symbol": "",
          "join_categories_id": 46,
          "join_subcategories_id": 207,
          "join_subsubcategories_id": 0,
          "join_categories": "46,207",
          "companies_price": "0.00",
          "companies_address": "488 Madison Ave., 4th Fl.",
          "companies_city": "New York",
          "companies_state": "NY",
          "companies_zip": "10022",
          "companies_country": "US",
          "companies_website": "http://www.agency.com",
          "companies_phone": "(212) 358-2600",
          "companies_sales_phone": "",
          "companies_customer_service_phone": "",
          "companies_fax": "(212) 358-2604",
          "companies_founded": 0,
          "companies_employee_count": 0,
          "companies_revenue": 0,
          "companies_accredited": 1,
          "companies_industry": "0",
          "companies_bbb_rating": "0",
          "companies_bbb_accredited": 0,
          "companies_leader_name": "Chan Suh",
          "companies_leader_phone": "",
          "companies_leader_email": "",
          "companies_contact_detail": "",
          "companies_description": "",
          "companies_description_meta": "",
          "companies_description_internal": "Sometimes the name says it all. Agency.com is a leading interactive marketing firm that specializes in creating websites for large corporate clients. Its work includes corporate branding sites, online marketing sites, and e-commerce systems. It also offers media planning and buying services. In addition, Agency.com is active in creating new types of interactive marketing using outdoor displays, mobile phones, and interactive television. Serving such notable clients as 3M, British Airways, Energizer, and Visa, it operates about 10 offices in the US, Europe, and the Asia/Pacific. Agency.com is a part of the Diversified Agency Services division of advertising and media services giant Omnicom Group.",
          "companies_opinion": "",
          "companies_pros_cons": "",
          "companies_rate_overall": 0,
          "companies_rate_csr": 0,
          "companies_rate_csa": 0,
          "companies_rate_q": 0,
          "companies_rate_pr": 0,
          "companies_size": "large",
          "companies_type": "srv,b2b",
          "companies_date_added": "2013-01-19 16:03:21",
          "companies_live": 1,
          "companies_group_code": "",
          "companies_announcement": "",
          "companies_sales_email": "",
          "companies_support_email": "",
          "companies_strengths": "",
          "testimonials": [],
          "category": {
            "categories_id": 46,
            "join_categories_id": 0,
            "categories_name": "Internet and Web",
            "categories_name_url": "internet-and-web",
            "categories_companies_count": 87
          }
        }
         */


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
            if (obj.has("companies_revenue")) {
                revenue = obj.getInt("companies_revenue");
            }
            if (obj.has("companies_founded")) {
                founded = obj.getInt("companies_founded");
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionInternal() {
        return description_internal;
    }

    public void setDescriptionInternal(String description_internal) {
        this.description_internal = description_internal;
    }

    public String getDescriptionMeta() {
        return description_meta;
    }

    public void setDescriptionMeta(String description_meta) {
        this.description_meta = description_meta;
    }

    public Integer getEmployeesNo() {
        return employees_no;
    }

    public void setEmployeesNo(Integer employees_no) {
        this.employees_no = employees_no;
    }

    public String getCityStateZip() {
        StringBuilder sb = new StringBuilder();

        if (!city.isEmpty()) {
            sb.append(city);
            if (!state.isEmpty() || !zip.isEmpty()) {
                sb.append(", ");
            }
        }

        if (!state.isEmpty()) {
            sb.append(state);
            if (!zip.isEmpty()) {
                sb.append(" ");
            }
        }

        if (!zip.isEmpty()) {
            sb.append(zip);
        }

        return sb.toString();
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }
}
