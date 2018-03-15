package app.touched.com.touched.Activites;

/**
 * Created by apple on 16/02/18.
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.touched.com.touched.Adapter.SlidingImage_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.Models.Users;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.DialogsUtils;
import app.touched.com.touched.Utilities.LocationManagerUtility;
import app.touched.com.touched.Utilities.TimeUtils;

import static app.touched.com.touched.Utilities.Utility.tryGetValueFromString;

public class SlidingActivity extends BaseActivity implements View.OnClickListener {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.one, R.drawable.two, R.drawable.three};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ImageView fbImageButton;
    CallbackManager mCallbackManager;
    LoginButton loginButton;
    FirebaseAuth mAuth;
    //String user_id;
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
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                handleFacebookAccessToken(currentAccessToken);
            }
        };
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//            }
//        };
//        mAuth.addAuthStateListener(mAuthStateListener);
        init();
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
        //Collections.addAll(ImagesArray,IMAGES);
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(SlidingActivity.this, ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        fbImageButton = (ImageView) findViewById(R.id.imv_fb_icon);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ((MainApplicationClass) getApplication()).setAccessToken(loginResult.getAccessToken());
                accessToken = loginResult.getAccessToken();
                Log.d("", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

//photos{album,from,icon,id,updated_time,images,link,name,height}
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
                LoginManager.getInstance().logInWithReadPermissions(SlidingActivity.this, Arrays.asList("public_profile", "email", "user_photos", "user_about_me", "user_birthday", "user_education_history", "user_hometown", "user_location", "user_work_history", "user_events"));

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
            parameters.putString("fields", "id,email,first_name,gender,last_name,location,about,picture,address,birthday,education,work,interested_in");

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
                                user_details.setAge(age);
                            } else {
                                user_details.setAge("18");
                            }
                            user_details.setNo_gifts("0");
                            user_details.setNo_refunds("0");
                            user_details.setPoints(0);

                            uploadUserDetailsToDB(user_details, user);

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

    private void uploadUserDetailsToDB(User_Details user_details, FirebaseUser firebaseUser) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Users users = new Users(user_details.getUser_id(), user_details.getEmail(), TimeUtils.getCurrentDateTime(), "true", TimeUtils.getCurrentDateTime());
        users.setMsg_count("0");
        users.setGifts_counts("0");
        users.setRefund_amount("0");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.USERS_Details_NODE + "/" + firebaseUser.getUid(), user_details);
        childUpdates.put("/" + Constants.USERS_NODE + "/" + firebaseUser.getUid(), users);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        Log.e("access token", accessToken.getUserId());
        Log.e("user id", user_details.getUser_id());

        ((MainApplicationClass) getApplication()).setProfileUsersDetail(user_details);
        DialogsUtils.hideProgressDialog();
        startActivity(new Intent(SlidingActivity.this, MainActivity.class));
        finish();


    }
}
