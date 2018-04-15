package app.touched.com.touched.Activites;

/**
 * Created by apple on 16/02/18.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import app.touched.com.touched.Fragments.SliderImagesFragment;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.Models.Users;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.DialogsUtils;
import app.touched.com.touched.Utilities.LocationManagerUtility;
import app.touched.com.touched.Utilities.TimeUtils;

import static app.touched.com.touched.Utilities.Constants.SPLASH_SCREEN_NODE;
import static app.touched.com.touched.Utilities.Utility.tryGetValueFromString;

public class SlidingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView fbImageButton;
    CallbackManager mCallbackManager;
    StorageReference splashScreenStorage;
    FirebaseAuth mAuth;

    AccessToken accessToken;
    private DatabaseReference mDatabaseUsersDetails, mDatabaseUsers, mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private AccessTokenTracker mFacebookAccessTokenTracker;
    LocationManagerUtility locationManagerUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManagerUtility = new LocationManagerUtility(SlidingActivity.this);
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.slider_layout);
        mAuth = ((MainApplicationClass) this.getApplication()).getmAuth();
        splashScreenStorage = ((MainApplicationClass) this.getApplication()).getStorageRef().child(SPLASH_SCREEN_NODE);
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                handleFacebookAccessToken(currentAccessToken);
            }
        };
        addFragment();
        init();
    }

    private void addFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, new SliderImagesFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManagerUtility.startUsingGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }
    }

    private void init() {


        fbImageButton = (ImageView) findViewById(R.id.imv_fb_icon);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ((MainApplicationClass) getApplication()).setAccessToken(loginResult.getAccessToken());
                accessToken = loginResult.getAccessToken();
                Log.d("", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

// photos{album,from,icon,id,updated_time,images,link,name,height}
// interested_in,birthday,favorite_athletes,favorite_teams,inspirational_people,sports
            }

            @Override
            public void onCancel() {
                Log.d("", "facebook:onCancel");
                DialogsUtils.hideProgressDialog();
                Toast.makeText(SlidingActivity.this, "Login cancelled by user. please try again to proceed", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("", "facebook:onError", error);
                DialogsUtils.hideProgressDialog();
                Toast.makeText(SlidingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                // ...
            }
        });
        fbImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  loginButton.performClick();
                DialogsUtils.showProgressDialog(SlidingActivity.this, "Login", "Please wait while we are connecting you to the facebook");
                LoginManager.getInstance().logInWithReadPermissions(SlidingActivity.this, Arrays.asList("public_profile", "email", "user_photos", "user_birthday", "user_hometown", "user_location", "user_events"));

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("", "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //  showProgressDialog();
        // [END_EXCLUDE]
        DialogsUtils.updateProgressDialogMessage(SlidingActivity.this, "We are Sign up your details. please wait...");
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //    hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]


    private void updateUI(final FirebaseUser user) {
        if (user != null) {
            // login
            Log.e("user ", user.getUid());
            ((MainApplicationClass) this.getApplication()).setMyDetails(user);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,first_name,gender,last_name,location,about,picture.height(1000),address,birthday,education,work,interested_in");

            GraphRequest request = GraphRequest.newMeRequest(

                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", object.toString());

                            // Application code
                            // user_id = object.getString("id");
                            User_Details user_details = new Gson().fromJson(object.toString(), User_Details.class);
                            String dob = tryGetValueFromString("birthday", object);
                            if (dob != null) {

                                String[] splitDate = dob.split("/");
                                String age = TimeUtils.getAge(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[0]));
                                user_details.setAge(Integer.parseInt(age));
                            } else {
                                user_details.setAge(18);
                            }
                            user_details.setNo_gifts("0");
                            user_details.setNo_refunds("0");
                            user_details.setPoints(0);
                            if (user_details.getLocation() != null) {
                                user_details.getLocation().setLatitude(locationManagerUtility.getLatitude());
                                user_details.getLocation().setLongitude(locationManagerUtility.getLongitude());
                            } else {
                                User_Details.Location location = new User_Details.Location();
                                location.setLongitude(locationManagerUtility.getLongitude());
                                location.setLatitude(locationManagerUtility.getLatitude());
                            }
                            getPhotosFromTheFb(user_details, user);

                            // 01/31/1980 format
                        }

                    });
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            DialogsUtils.hideProgressDialog();
        }
//        Intent in = new Intent(getApplicationContext(),HomePage.class);
//        startActivity(in);
    }

    private void getPhotosFromTheFb(final User_Details user_details, final FirebaseUser user) {
        new GraphRequest(
                accessToken,
                "/" + user_details.getUser_id() + "/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Gson gson = new Gson();
//                        JsonReader reader = new JsonReader(new StringReader(response.toString()));
//                        reader.setLenient(true);
                        User_Details only_photos = gson.fromJson(response.getJSONObject().toString(), User_Details.class);
// when we get photos from fb then try to save directly on own storage so that we can use it latter
                        // User_Details only_photos = new Gson().fromJson(response.toString(), User_Details.class);
                        user_details.setPhotos(only_photos.getPhotos());
                        uploadUserDetailsToDB(user_details, user);

                    }
                }
        ).executeAsync();
    }

    private void uploadUserDetailsToDB(User_Details user_details, FirebaseUser firebaseUser) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.e("access token", accessToken.getUserId());
        Log.e("user id", user_details.getUser_id());
        user_details.setUser_id(firebaseUser.getUid());
        Users users = new Users(user_details.getUser_id(), user_details.getEmail(), TimeUtils.getCurrentDateTime(), "true", TimeUtils.getCurrentDateTime());

        users.setGifts_counts("0");
        users.setRefund_amount("0");
        user_details.setIs_login("true");
        user_details.setMsg_count("0");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.USERS_Details_NODE + "/" + firebaseUser.getUid(), user_details);
        childUpdates.put("/" + Constants.USERS_NODE + "/" + firebaseUser.getUid(), users);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

        ((MainApplicationClass) getApplication()).setProfileUsersDetail(user_details);
        DialogsUtils.hideProgressDialog();
        startActivity(new Intent(SlidingActivity.this, MainActivity.class));
        finish();


    }
}
