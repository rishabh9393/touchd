package app.touched.com.touched.Utilities;

/**
 * Created by user on 3/3/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import app.touched.com.touched.R;


public class DialogBox {
    private FragmentTransaction fragmentTransaction;
    private FragmentActivity activity;
    Boolean YesorNo = false;
    Boolean result;
    private FragmentManager fragmentManager;

    public DialogBox(Activity act) {
        activity = (FragmentActivity) act;

    }

//    public void emailAlert(String message) {
//        Button confirm;
//        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.custom_dialog);
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        confirm = (Button) dialog.findViewById(R.id.dialogButtonOK);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 fragmentManager =activity.getSupportFragmentManager() ;
//                fragmentManager.popBackStack();
//                fragmentTransaction=activity.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.acfrag_frame,new LoginFragment());
//                //  fragmentTransaction.addToBackStack("ForgetFragment");
//                fragmentTransaction.commit();
//                dialog.dismiss();
//
//            }
//        });
//
//        dialog.show();
//    }

    boolean accept;

    public Boolean ShowImage(String image) {
        ImageButton confirm;
        if (image != null) {
            final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.get_image);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            //confirm = (ImageButton) dialog.findViewById(R.id.send_Imgid);
            ImageView imageFullview = (ImageView) dialog.findViewById(R.id.image_full_view);
            imageFullview.setImageURI(Uri.parse(image));


            dialog.show();
        }
        return accept;
    }

}

