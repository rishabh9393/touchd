package app.touched.com.touched.Utilities;

/**
 * Created by user on 3/3/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.R;


public class DialogBox {
    private FragmentTransaction fragmentTransaction;
    private FragmentActivity activity;
    Boolean YesorNo = false;
    Boolean result;
    private FragmentManager fragmentManager;
    private StorageReference downloadUrl;
    private FirebaseStorage storage;

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

    public Boolean ShowImage(String image,String id) {
        ImageButton confirm;
        if (image != null) {
            final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.get_image);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            //confirm = (ImageButton) dialog.findViewById(R.id.send_Imgid);
            ImageView imageFullview = (ImageView) dialog.findViewById(R.id.image_full_view);

            Picasso.with(dialog.getContext()).load(Uri.parse(image)).networkPolicy(NetworkPolicy.OFFLINE).into(imageFullview);

            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "touchd";
            File file = new File(rootDir,id);
            if(file.exists())
            {
                // hide download button
            }
            else {
                //download image enabled
            }
            dialog.show();
        }
        return accept;
    }

    public void downloadImage(String image,String name) throws IOException {
        storage = ((MainApplicationClass) activity.getApplication()).getStorage();
        downloadUrl = storage.getReferenceFromUrl(image);
        String rootDir = Environment.getExternalStorageDirectory()
                + File.separator + "touchd";
        File rootFile = new File(rootDir);
        rootFile.mkdir();

        File newFile = new File(rootFile, name);
        newFile.createNewFile();
        downloadUrl.getFile(newFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}

