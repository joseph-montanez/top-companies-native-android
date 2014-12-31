package com.comentum.topcompanies.topcompanies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joseph on 12/25/2014.
 */
public class SearchItem {
    public SearchItem() {

    }

    public enum ItemType {
        Category,
        Company
    }

    protected String name;
    protected String id;
    protected ItemType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public SearchItem(String label, String value) throws ExceptionInInitializerError {
        name = label;
        Boolean matchedValue = parseValue(value);
        if (!matchedValue) {
            throw new ExceptionInInitializerError("Value does match the format '{typeType}_{id}'");
        }
    }

    public Boolean parseValue(String value) {
        Boolean valid;
        String[] parts = value.split("_");
        if (parts.length == 2) {
            id = parts[1];
            switch (parts[0]) {
                case "company":
                    type = ItemType.Company;
                    valid = true;
                    break;
                case "keyword":
                    type = ItemType.Category;
                    valid = true;
                    break;
                default:
                    valid = false;
                    break;
            }
        } else {
            valid = false;
        }

        return valid;
    }

    public void fromJson(JSONObject obj) {
        name = obj.optString("name", "");
        id = obj.optString("id", "");
        //-- Parse Enum
        String typeS = obj.optString("type", "");
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("id", id);
            obj.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
