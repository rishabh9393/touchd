package app.touched.com.touched;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import app.touched.com.touched.Activites.MainActivity;

public class HomePage extends AppCompatActivity {
Button logout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
       // logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent in=new Intent(HomePage.this, MainActivity.class);
                startActivity(in);
            }
        });
    }
}
