package com.comentum.topcompanies.topcompanies;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.CompletionInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;

/**
 * Created by Joseph on 8/6/2014.
 */
public class CompleteTextView extends AutoCompleteTextView {
    protected String typed;
    protected String commitedCompletion;
    protected Integer commitedPosition;

    private AutoCompleteCommunication mViewCommunication;

    public interface AutoCompleteCommunication {
        public void selected(CompletionInfo completion);
    }

    @Override
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof AutoCompleteCommunication) {
            mViewCommunication = (AutoCompleteCommunication) adapter;
        }
    }

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
        SearchActivity.PlacesAutoCompleteAdapter adapter = (SearchActivity.PlacesAutoCompleteAdapter) this.getAdapter();
        Log.i("onCommitCompletion:text", completion.getText().toString());
        commitedCompletion = completion.getText().toString();
        commitedPosition = completion.getPosition();
        if (mViewCommunication != null) {
            mViewCommunication.selected(completion);
        }
        if (completion.getLabel() != null) {
            Log.i("onCommitCompletion:labe", completion.getLabel().toString());
        }
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
