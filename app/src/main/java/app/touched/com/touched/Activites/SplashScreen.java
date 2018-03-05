package app.touched.com.touched.Activites;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.R;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    private final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = ((MainApplicationClass) this.getApplication()).getmAuth();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        isUserLogin(currentUser);
    }

    private void isUserLogin(FirebaseUser currentUser) {
        if (currentUser != null) {
            ((MainApplicationClass) this.getApplication()).setMyDetails(currentUser);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
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
}
