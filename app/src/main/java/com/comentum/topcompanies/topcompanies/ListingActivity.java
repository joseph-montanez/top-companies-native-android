package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.comentum.topcompanies.topcompanies.Api.Api;

import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListingActivity extends Activity {
    private static final String LOG_TAG = "TopCompanies";
    private static final String TAG = "ListingActivity";
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";
    private static final String LIST_POSITION_KEY = "LIST_POSITION_KEY";
    private static final String ITEM_POSITION_KEY = "ITEM_POSITION_KEY";
    public JSONObject transport;
    public CompanyArrayAdapter adapter;
    public ListView listView;
    private Parcelable mListState = null;
    private Integer mListPosition = null;
    private Integer mItemPosition = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.animateBack(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_listing);

        final TextView listHeading = (TextView) findViewById(R.id.listHeading);
        Bundle extras = getIntent().getExtras();
        String payload = extras.getString("payload");
        Log.i("payload", payload);

        Helper.setUpBackButton(this);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new CompanyArrayAdapter(this, this, new ArrayList<Company>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyArrayAdapter adapter = (CompanyArrayAdapter) parent.getAdapter();
                Company company = adapter.getItemById(position);
//                final String item = (String) parent.getItemAtPosition(position);
                Log.i(TAG, "onItemClick: " + company.getName());


                //-- Call save instance before moving on
//                Bundle state = new Bundle();
//                activity.onSaveInstanceState(state);

                //-- start activity
                ListingActivity activity = ListingActivity.this;
                JSONObject transport = activity.transport;
                try {
                    transport.put("companies_id", company.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String payload = transport.toString();
                Intent intent = new Intent(activity, CompanyActivity.class);
                intent.putExtra("payload", payload);
                activity.startActivity(intent);
                Helper.animateForward(activity);

            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final String item = (String) adapterView.getItemAtPosition(i);
//                Log.i(LOG_TAG, "OnItemClickListener: " + item);
//            }
//        });
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Exec async load task
        final ProgressDialog dialog = new ProgressDialog(ListingActivity.this);
        dialog.setMessage("Downloading companies...");
        dialog.show();

        try {
            transport = new JSONObject(payload);
            SearchItem item = new SearchItem();
            JSONObject jsonItem = transport.optJSONObject("item");
            if (jsonItem != null) {
                item.fromJson(jsonItem);
            }
            listHeading.setText(item.getName());
            Api api = new Api();
            Promise listing = api.listing(item.getId());
            listing.then(new DoneCallback<ArrayList<Company>>() {
                @Override
                public void onDone(ArrayList<Company> result) {
                    dialog.dismiss();

                    adapter = new CompanyArrayAdapter(ListingActivity.this, ListingActivity.this, result);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
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

    @Override
     public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        if (mListState != null) {
            getListView().onRestoreInstanceState(mListState);
        }
        if (mListPosition != null && mItemPosition != null) {
            listView.setSelectionFromTop(mListPosition, mItemPosition);
        }
        mListState = null;
        mListPosition = null;
        mItemPosition = null;
//        adapter.setItemList(dbHelper.getItems());
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.notifyDataSetInvalidated();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.i(LOG_TAG, "onSaveInstanceState");
        mListState = getListView().onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);

        // Save position of first visible item
        mListPosition = listView.getFirstVisiblePosition();
        state.putInt(LIST_POSITION_KEY, mListPosition);

        // Save scroll position of item
        View itemView = listView.getChildAt(0);
        mItemPosition = itemView == null ? 0 : itemView.getTop();
        state.putInt(ITEM_POSITION_KEY, mItemPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.i(LOG_TAG, "onRestoreInstanceState");
        mListState = state.getParcelable(LIST_STATE_KEY);
        mListPosition = state.getInt(LIST_POSITION_KEY);
        mItemPosition = state.getInt(ITEM_POSITION_KEY);
    }

    public ListView getListView() {
        return listView;
    }
}
