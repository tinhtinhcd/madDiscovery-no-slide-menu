package com.anhlt.maddiscover.services;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.anhlt.maddiscover.R;

/**
 * Created by anhlt on 4/3/16.
 */
public class ApplicationService {

    public static void showErrorDialog(String title, String message, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
//            builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}
