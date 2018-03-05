package app.touched.com.touched.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import app.touched.com.touched.Activites.MainActivity;

/**
 * Created by Anshul on 2/24/2018.
 */

public class DialogsUtils {
    private static ProgressDialog progressDialog;
    private static String newProgressMessage;

    public static void showProgressDialog(Activity context, String title, String message) {

        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage(message); // Setting Message
        progressDialog.setTitle(title); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    public static void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void updateProgressDialogMessage(Activity context, final String message) {
        newProgressMessage = message;
        context.runOnUiThread(changeMessage);

    }

    private static Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            //Log.v(TAG, strCharacters);
            progressDialog.setMessage(newProgressMessage);
        }
    };

}
