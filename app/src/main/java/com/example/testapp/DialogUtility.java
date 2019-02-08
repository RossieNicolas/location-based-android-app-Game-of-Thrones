package com.example.testapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.net.ContentHandler;

public class DialogUtility {
    public static void skipTask(Context context, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("skip the task?")
                .setMessage("Would you like to skip the task?")
                .setCancelable(false)
                .setPositiveButton("Yes", onClickListener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
