package com.comentum.topcompanies.topcompanies;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.CompletionInfo;
import android.widget.AutoCompleteTextView;

/**
 * Created by Joseph on 8/6/2014.
 */
public class CompleteTextView extends AutoCompleteTextView {
    protected String typed;

    public CompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompleteTextView(Context context) {
        super(context);
    }

    @Override
    public void onCommitCompletion(CompletionInfo completion) {
        Log.i("onCommitCompletion text", completion.getText().toString());
        Log.i("onCommitCompletion label", completion.getLabel().toString());
    }

    @Override
    public void replaceText(CharSequence text) {
//        typed = getText().toString();
//        super.replaceText(text);
    }

    public String getTyped() {
        return getText().toString();
    }
}
