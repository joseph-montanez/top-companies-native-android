package com.comentum.topcompanies.topcompanies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 8/5/2014.
 */
public class Label {
    protected String label;
    protected String value;
    protected String type;

    public Label() {

    }

    public void fromJson(JSONObject obj) {
        try {
            label = obj.getString("label");
            String valueType = obj.getString("value");
            String[] parts = valueType.split("_");
            type = parts[0];
            value = parts[1];
        } catch (JSONException e) {
            Log.e("TopComapanies", "Cannot process JSON results", e);
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("label", label);
            obj.put("value", value);
            obj.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public String getLabel() {
        return label;
    }
}
