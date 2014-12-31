package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import com.comentum.topcompanies.topcompanies.Api.Api;

import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity {
    private static final String LOG_TAG = "TopCompanies";
    private final Api api = new Api();
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        final CompleteTextView autoCompView = (CompleteTextView) findViewById(R.id.keywordTxt);
        //-- Auto focus with keyboard
        autoCompView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(autoCompView, InputMethodManager.SHOW_IMPLICIT);

        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        final Context context = getApplicationContext();

        loading = (ProgressBar) findViewById(R.id.loadingIndicator);
        loading.setVisibility(View.INVISIBLE);


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
                SearchItem item = adapter.getSearchItem(i);

                //-- Finish and animate
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

                //-- start activity
                JSONObject transport = new JSONObject();
                try {
                    transport.put("item", item.toJson());
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

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<SearchItem> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            SearchItem item = resultList.get(index);
            return resultList.get(index).getName();
        }

        public SearchItem getSearchItem(int index) {
            SearchItem item = resultList.get(index);
            return item;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(final CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.

                        final Promise search = api.search(constraint.toString());
                        SearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                loading.setVisibility(View.VISIBLE);
                            }
                        });

                        search.done(new DoneCallback<ArrayList<SearchItem>>() {
                            @Override
                            public void onDone(ArrayList<SearchItem> items) {
                                resultList = items;
                                // Assign the data to the FilterResults
                                filterResults.values = resultList;
                                filterResults.count = resultList.size();
                                publishResults(constraint, filterResults);
                                SearchActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        loading.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        });

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
