package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.comentum.topcompanies.topcompanies.Api.Api;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class CompanyActivity extends Activity {
    protected String LOG_TAG = "TopCompanies";
    public JSONObject transport;

    // UI Elements
    private LinearLayout listCompanyDetails;

    private TextView listCompanyName;
    private TextView listCompanyAddress;
    private TextView listCompanyCityStateZip;
    private TextView listCompanyWebsite;
    private TextView listCompanyPhone;

    private LinearLayout listCompanyOverviewGroup;
    private TextView listCompanyNameOverview;

    private LinearLayout listCompanyDescriptionGroup;
    private TextView listCompanyDescription;

    private LinearLayout listCompanyBusinessInformationGroup;

    private TableRow listCompanyFoundedGroup;
    private TextView listCompanyFounded;
    private TableRow listCompanyRevenueGroup;
    private TextView listCompanyRevenue;
    private TableRow listCompanyEmployeeGroup;
    private TextView listCompanyEmployee;

    private class Payload {
        private Integer companiesId;
        private String typed;
        private SearchItem item;

        public Integer getCompaniesId() {
            return companiesId;
        }

        public void setCompaniesId(Integer companiesId) {
            this.companiesId = companiesId;
        }

        public String getTyped() {
            return typed;
        }

        public void setTyped(String typed) {
            this.typed = typed;
        }

        public SearchItem getItem() {
            return item;
        }

        public void setItem(SearchItem item) {
            this.item = item;
        }

        public void fromJson(JSONObject transport) {
            setCompaniesId(transport.optInt("companies_id", 0));
            setTyped(transport.optString("typed", ""));
            SearchItem item = new SearchItem();
            JSONObject itemJson = transport.optJSONObject("item");
            if (itemJson != null) {
                item.fromJson(itemJson);
            }
            setItem(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras;
        String payloadData;
        Payload payload;
        Api api;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        final ImageView backButton = (ImageView) findViewById(R.id.listViewBackButton);
        backButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final TextView listHeading = (TextView) findViewById(R.id.listHeading);

        final ImageView listCompanyImage = (ImageView) findViewById(R.id.listCompanyImage);
        Backdrop bd = new Backdrop();
        listCompanyImage.setImageResource(bd.getRandomImage());

        setUpViewElements();
        listCompanyDetails.setVisibility(View.INVISIBLE);

        //-- Load Company
        extras = getIntent().getExtras();
        api = new Api();
        payload = new Payload();
        payloadData = extras.getString("payload");

        try {
            transport = new JSONObject(payloadData);
            payload.fromJson(transport);
            listHeading.setText(payload.getItem().getName());
            Promise p1 = api.detail(payload.getCompaniesId());
            p1.then(new DoneCallback<Company>() {
                @Override
                public void onDone(Company company) {
                    renderCompany(company);
                }
            });
            p1.fail(new FailCallback() {
                @Override
                public void onFail(Object result) {
                    Log.d("CompanyActivity", "failed to load company");
                }
            });
        }  catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_to_right, R.anim.push_right_to_left);
        finish();

        Intent intent = this.getIntent();
        intent.putExtra("payload", transport.toString());
        intent.setClass(this, ListingActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpViewElements() {
        listCompanyDetails = (LinearLayout) findViewById(R.id.listCompanyDetails);

        listCompanyName = (TextView) findViewById(R.id.listCompanyName);
        listCompanyAddress = (TextView) findViewById(R.id.listCompanyAddress);
        listCompanyCityStateZip = (TextView) findViewById(R.id.listCompanyCityStateZip);
        listCompanyWebsite = (TextView) findViewById(R.id.listCompanyWebsite);
        listCompanyPhone = (TextView) findViewById(R.id.listCompanyPhone);

        listCompanyOverviewGroup = (LinearLayout) findViewById(R.id.listCompanyOverviewGroup);
        listCompanyNameOverview = (TextView) findViewById(R.id.listCompanyNameOverview);

        listCompanyDescriptionGroup = (LinearLayout) findViewById(R.id.listCompanyDescriptionGroup);
        listCompanyDescription = (TextView) findViewById(R.id.listCompanyDescription);

        listCompanyBusinessInformationGroup = (LinearLayout) findViewById(R.id.listCompanyBusinessInformationGroup);

        listCompanyFoundedGroup = (TableRow) findViewById(R.id.listCompanyFoundedGroup);
        listCompanyFounded = (TextView) findViewById(R.id.listCompanyFounded);
        listCompanyRevenueGroup = (TableRow) findViewById(R.id.listCompanyRevenueGroup);
        listCompanyRevenue = (TextView) findViewById(R.id.listCompanyRevenue);
        listCompanyEmployeeGroup = (TableRow) findViewById(R.id.listCompanyEmployeeGroup);
        listCompanyEmployee = (TextView) findViewById(R.id.listCompanyEmployee);
    }

    public void renderCompany(Company company) {
        listCompanyName.setText(company.getName());

        if (company.getAddress().isEmpty()) {
            listCompanyAddress.setVisibility(View.GONE);
        } else {
            listCompanyAddress.setVisibility(View.VISIBLE);
            listCompanyAddress.setText(company.getAddress());
        }

        if (company.getCityStateZip().isEmpty()) {
            listCompanyCityStateZip.setVisibility(View.GONE);
        } else {
            listCompanyCityStateZip.setVisibility(View.VISIBLE);
            listCompanyCityStateZip.setText(company.getCityStateZip());
        }

        if (company.getWebsite().isEmpty()) {
            listCompanyWebsite.setVisibility(View.GONE);
        } else {
            listCompanyWebsite.setVisibility(View.VISIBLE);
            listCompanyWebsite.setText(company.getWebsite());
        }

        if (company.getPhone().isEmpty()) {
            listCompanyPhone.setVisibility(View.GONE);
        } else {
            listCompanyPhone.setVisibility(View.VISIBLE);
            listCompanyPhone.setText(company.getPhone());
        }

        if (company.getDescription().isEmpty()) {
            listCompanyOverviewGroup.setVisibility(View.GONE);
        } else {
            listCompanyOverviewGroup.setVisibility(View.VISIBLE);
            listCompanyNameOverview.setText(company.getDescription());
        }

        if (company.getDescriptionMeta().isEmpty()) {
            listCompanyDescriptionGroup.setVisibility(View.GONE);
        } else {
            listCompanyDescriptionGroup.setVisibility(View.VISIBLE);
            listCompanyDescription.setText(company.getDescriptionMeta());
        }

        if (company.getEmployeesNo() + company.getFounded() + company.getRevenue() > 0) {
            listCompanyBusinessInformationGroup.setVisibility(View.VISIBLE);

            if (company.getFounded() > 0) {
                listCompanyFoundedGroup.setVisibility(View.VISIBLE);
                listCompanyFounded.setText(company.getFounded());
            } else {
                listCompanyFoundedGroup.setVisibility(View.GONE);
            }

            if (company.getRevenue() > 0) {
                listCompanyRevenueGroup.setVisibility(View.VISIBLE);
                listCompanyRevenue.setText(company.getRevenue());
            } else {
                listCompanyRevenueGroup.setVisibility(View.GONE);
            }

            if (company.getEmployeesNo() > 0) {
                listCompanyEmployeeGroup.setVisibility(View.VISIBLE);
                listCompanyEmployee.setText(company.getEmployeesNo());
            } else {
                listCompanyEmployeeGroup.setVisibility(View.GONE);
            }

        } else {
            listCompanyBusinessInformationGroup.setVisibility(View.GONE);
        }

        listCompanyDetails.setVisibility(View.VISIBLE);
    }
}
