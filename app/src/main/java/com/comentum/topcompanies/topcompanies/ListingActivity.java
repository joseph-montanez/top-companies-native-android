package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class ListingActivity extends Activity {
    private static final String LOG_TAG = "TopCompanies";
    public JSONObject transport;
    public CompanyArrayAdapter adapter;
    public ListView listView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.animateBack(this);

//
//        Intent intent = this.getIntent();
//        intent.putExtra("payload", transport.toString());
//        intent.setClass(this, SearchActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        final TextView listHeading = (TextView) findViewById(R.id.listHeading);
        Bundle extras = getIntent().getExtras();
        String payload = extras.getString("payload");
        Log.i("payload", payload);

        final ImageView backButton = (ImageView) findViewById(R.id.listViewBackButton);
        backButton.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listView = (ListView) findViewById(R.id.listview);
        adapter = new CompanyArrayAdapter(this, this, new ArrayList<Company>());
        listView.setAdapter(adapter);
        listView.setOnItemSelectedListener(new ListView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                final String item = (String) parent.getItemAtPosition(i);
                Log.i(LOG_TAG, "onItemSelected: " + item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String item = (String) adapterView.getItemAtPosition(i);
                Log.i(LOG_TAG, "OnItemClickListener: " + item);
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Exec async load task

        try {
            transport = new JSONObject(payload);
            SearchItem item = new SearchItem();
            JSONObject jsonItem = transport.optJSONObject("item");
            if (jsonItem != null) {
                item.fromJson(jsonItem);
            }
            listHeading.setText(item.getName());
            StringBuilder sb = new StringBuilder("http://hawk2.comentum.com/topcompanies/app-api/ajax-listing.php");
            sb.append("?companies=true");
            sb.append("&keywords_id=" + URLEncoder.encode(item.getId(), "utf8"));
            (new AsyncListViewLoader()).execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listing, menu);
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
    private class AsyncListViewLoader extends AsyncTask<String, Void, List<Company>> {
        private final ProgressDialog dialog = new ProgressDialog(ListingActivity.this);

        @Override
        protected void onPostExecute(final List<Company> result) {
            Log.i(LOG_TAG, "onPostExecute: " + Integer.toString(result.size()));
            super.onPostExecute(result);
            dialog.dismiss();

            adapter = new CompanyArrayAdapter(ListingActivity.this, ListingActivity.this, result);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading companies...");
            dialog.show();
        }

        @Override
        protected List<Company> doInBackground(String... params) {
            List<Company> resultList = new ArrayList<Company>();
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
                JSONArray companies = obj.getJSONArray("companies");

                Log.i(LOG_TAG, "length JSON: " + Integer.toString(result.length()));
                Log.i(LOG_TAG, "Number of companies: " + Integer.toString(companies.length()));
                for (int i=0; i < companies.length(); i++) {
                    Company company = new Company();
                    company.fromJson(companies.getJSONObject(i));
                    resultList.add(company);
                }

                return resultList;
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error processing Top Companies API URL", e);
                return resultList;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to Top Companies API", e);

                return resultList;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing to Top Companies API", e);

                return resultList;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    @Override
     public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
//        adapter.setItemList(dbHelper.getItems());
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.notifyDataSetInvalidated();
    }
}
