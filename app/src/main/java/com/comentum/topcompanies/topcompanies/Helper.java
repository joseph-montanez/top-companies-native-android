package com.comentum.topcompanies.topcompanies;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by hippo-pc on 1/2/2015.
 */
public class Helper {
    public static void animateBack(Activity act) {
        act.overridePendingTransition(R.anim.push_left_to_right, R.anim.push_right_to_right);
    }
    public static void animateForward(Activity act) {
        act.overridePendingTransition(R.anim.push_left_to_left, R.anim.push_right_to_left);
    }

    public static void setUpBackButton(final Activity act) {
        final Button backButton = (Button) act.findViewById(R.id.listViewBackButton);
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                act.onBackPressed();
            }
        });
    }
}
