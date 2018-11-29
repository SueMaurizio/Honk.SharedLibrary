package org.honk.sharedlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UIHelper {

    public static void showAlert(String message, Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(R.string.alertTitle)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                })
                .show();
    }

    public static void showAlert(String message, Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(R.string.alertTitle)
                .setPositiveButton(R.string.close, onClickListener)
                .show();
    }
}
