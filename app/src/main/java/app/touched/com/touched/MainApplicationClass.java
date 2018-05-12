package app.touched.com.touched;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.Utilities.Constants;

/**
 * Created by Anshul on 2/23/2018.
 */

public class MainApplicationClass extends Application {
    private FirebaseAuth mAuth;
    private AccessToken accessToken;
    private FirebaseUser myDetails;
    private User_Details profileUsersDetail;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public StorageReference getStorageRef() {
        return storageRef;
    }

    public void setStorageRef(StorageReference storageRef) {
        this.storageRef = storageRef;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    public User_Details getProfileUsersDetail() {
        return profileUsersDetail;
    }

    public void setProfileUsersDetail(User_Details profileUsersDetail) {
        this.profileUsersDetail = profileUsersDetail;
    }

    public FirebaseUser getMyDetails() {
        return myDetails;
    }

    public void setMyDetails(FirebaseUser myDetails) {
        this.myDetails = myDetails;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        StrictMode.VmPolicy.Builder builderVm = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderVm.build());
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(Constants.MSG_META);
        // save the data in offline mode and put database ref.sync true any where
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // save image in offline mode with picaso
        Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

    }


}
