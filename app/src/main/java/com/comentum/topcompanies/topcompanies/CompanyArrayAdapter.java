package com.comentum.topcompanies.topcompanies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hippo-pc on 8/6/2014.
 */
public class CompanyArrayAdapter extends ArrayAdapter<Company> {
    private final Context context;
    private final ListingActivity activity;
    private List<Company> values;

    public CompanyArrayAdapter(Context context, ListingActivity activity, List<Company> values) {
        super(context, R.layout.list_company, values);
        this.context = context;
        this.values = values;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_company, null);
        } else {
            v = convertView;
        }

        Log.i("position", Integer.toString(position));
        final Company c = values.get(position);
        //listCompanyName
        TextView listCompanyName = (TextView) v.findViewById(R.id.listCompanyName);
        TextView listCompanyAddress = (TextView) v.findViewById(R.id.listCompanyAddress);
        TextView listCompanyCityStateZip = (TextView) v.findViewById(R.id.listCompanyCityStateZip);
        TextView listCompanyPhone = (TextView) v.findViewById(R.id.listCompanyPhone);
        TextView listCompanyWebsite = (TextView) v.findViewById(R.id.listCompanyWebsite);

        listCompanyName.setText(c.name);
        listCompanyAddress.setText(c.address);
        listCompanyCityStateZip.setText(c.city + "," + c.state + " " + c.zip);
        listCompanyPhone.setText(c.phone);
        listCompanyWebsite.setText(c.website);

        return v;
    }

    public List<Company> getItemList() {
        return values;
    }

    public Company getItemById(int i) {
        return values.get(i);
    }

    public void setItemList(List<Company> itemList) {
        this.values = itemList;
    }
}