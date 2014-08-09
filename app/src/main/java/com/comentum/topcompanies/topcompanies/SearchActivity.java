package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends Activity {
    private static final String LOG_TAG = "TopCompanies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final CompleteTextView autoCompView = (CompleteTextView) findViewById(R.id.keywordTxt);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        final Context context = getApplicationContext();


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("payload")) {
            String payload = extras.getString("payload");

            try {
                JSONObject transport = new JSONObject(payload);
                String txt = transport.getString("typed");
                autoCompView.setText(txt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(LOG_TAG, "onItemClick: " + autoCompView.getText().toString());
                Log.i(LOG_TAG, "type: " + autoCompView.getTyped());
                PlacesAutoCompleteAdapter adapter = (PlacesAutoCompleteAdapter) adapterView.getAdapter();
                Label label = adapter.getLabel(i);

                //-- Finish and animate
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

                //-- start activity
                JSONObject transport = new JSONObject();
                try {
                    transport.put("label", label.toJson());
                    transport.put("typed", autoCompView.getTyped());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String payload = transport.toString();
                Intent intent = new Intent(SearchActivity.this, ListingActivity.class);
                intent.putExtra("payload", payload);
                startActivity(intent);

                //-- TODO detect company versus keyword result
            }
        });
    }

    private ArrayList<Label> autocomplete(String input) {
        ArrayList<Label> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("http://hawk2.comentum.com/topcompanies/app-api/related-keywords.php");
            sb.append("?term=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 9.0; WIndows NT 9.0; en-US))");
            InputStreamReader is = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = is.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Top Companies API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Top Companies API", e);

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONArray jsonArray = new JSONArray(jsonResults.toString());

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Label>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                Label l = new Label();
                l.fromJson(jsonArray.getJSONObject(i));
                resultList.add(l);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<Label> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            Label label = resultList.get(index);
            return resultList.get(index).getLabel();
        }

        public Label getLabel(int index) {
            Label label = resultList.get(index);
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
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
}
