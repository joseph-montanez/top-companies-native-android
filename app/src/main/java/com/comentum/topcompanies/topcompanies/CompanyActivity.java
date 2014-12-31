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
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //-- Load Company
        Bundle extras = getIntent().getExtras();
        String payload = extras.getString("payload");
        Log.i("payload", payload);
        //{"companies_id":1176,"typed":"web","item":{"id":"402","type":"Category","name":"Web Design - Corporate"}}

        try {
            transport = new JSONObject(payload);
            JSONObject item = transport.getJSONObject("item");
            String companies_id = transport.optString("companies_id", "0");
            listHeading.setText(item.optString("name", ""));
            StringBuilder sb = new StringBuilder("http://hawk2.comentum.com/topcompanies/app-api/ajax-details.php");
            sb.append("?companies_id=" + URLEncoder.encode(companies_id, "utf8"));
            (new AsyncCompanyLoader()).execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            // Its hitting this point
            e.printStackTrace();
        } catch (JSONException e) {
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

    private class AsyncCompanyLoader extends AsyncTask<String, Void, Company> {
        private final ProgressDialog dialog = new ProgressDialog(CompanyActivity.this);

        @Override
        protected void onPostExecute(final Company result) {
            super.onPostExecute(result);
            dialog.dismiss();

            //-- TODO: Bind company to data

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading companies...");
            dialog.show();
        }

        @Override
        protected Company doInBackground(String... params) {
            Company company = new Company();
            HttpURLConnection conn = null;
            try {
                URL u = new URL(params[0]);

                conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 9.0; WIndows NT 9.0; en-US))");
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(conn.getInputStream()));

                // Read the stream
                String line = "";
                String result = "";
                while((line = bufferedReader.readLine()) != null)
                    result += line;

                JSONObject obj = new JSONObject(result);

                Log.i(LOG_TAG, "length JSON: " + Integer.toString(result.length()));
                company.fromJson(obj);

                return company;
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error processing Top Companies API URL", e);
                return company;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to Top Companies API", e);

                return company;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing to Top Companies API", e);

                return company;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
