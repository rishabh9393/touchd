package app.touched.com.touched.Activites;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.touched.com.touched.Models.Users;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.TimeUtils;

import static app.touched.com.touched.Utilities.Constants.USER_LAST_ONLINE_TIME_NODE;

/**
 * Created by Anshul on 2/24/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private DatabaseReference dbUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        dbUsers = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_NODE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Users users=new Users();
        users.setIs_login("true");
        //dbUsers.child("").child(USER_LAST_ONLINE_TIME_NODE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Users users=new Users();
        users.setIs_login("false");
        users.setLast_online_time(TimeUtils.getCurrentDateTime());
    }
}
