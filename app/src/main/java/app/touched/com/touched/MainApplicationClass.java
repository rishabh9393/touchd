package app.touched.com.touched;

import android.app.Application;
import android.content.ComponentCallbacks2;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

    }


}
