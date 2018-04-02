package app.touched.com.touched.Activites;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    private final int SPLASH_TIME_OUT = 2000;
    private DatabaseReference dbToCollectExploreData;
    User_Details profileUsersDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = ((MainApplicationClass) this.getApplication()).getmAuth();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        isUserLogin(currentUser);

    }

    private void isUserLogin(final FirebaseUser currentUser) {
        if (currentUser != null) {

            ((MainApplicationClass) this.getApplication()).setMyDetails(currentUser);
            dbToCollectExploreData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE).child(currentUser.getUid());
            dbToCollectExploreData.addValueEventListener(userDetailListner);


        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, SlidingActivity.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private ValueEventListener userDetailListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            dbToCollectExploreData.removeEventListener(userDetailListner);

            profileUsersDetail = dataSnapshot.getValue(User_Details.class);
            if (profileUsersDetail != null) {

                ((MainApplicationClass) SplashScreen.this.getApplication()).setProfileUsersDetail(profileUsersDetail);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
// ask  them how many developers you have for android, i dont want to work alone. in the single project
                finish();

            } else {
                Intent i = new Intent(SplashScreen.this, SlidingActivity.class);
                startActivity(i);
                finish();
            }


        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
}
