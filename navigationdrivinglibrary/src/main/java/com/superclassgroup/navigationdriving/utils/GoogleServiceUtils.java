package com.superclassgroup.navigationdriving.utils;

import android.app.Activity;
import android.app.Dialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by huyjackson on 7/17/17.
 */

public class GoogleServiceUtils {

    public static boolean googleServicesAvailable(Activity activity) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(activity);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(activity, isAvailable, 0);
            dialog.show();
        } else {
            //Toast.makeText(activity, "Can't connect google services!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
