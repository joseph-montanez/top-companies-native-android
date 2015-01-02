package com.comentum.topcompanies.topcompanies;

import android.app.Activity;

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
}
