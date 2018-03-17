package app.touched.com.touched.Activites;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.Users;
import app.touched.com.touched.Utilities.Constants;
import app.touched.com.touched.Utilities.PermissionUtility;
import app.touched.com.touched.Utilities.TimeUtils;

import static app.touched.com.touched.Utilities.Constants.IS_LOGIN_NODE;
import static app.touched.com.touched.Utilities.Constants.LAST_ONLINE_TIME_NODE;
import static app.touched.com.touched.Utilities.Constants.USER_LAST_ONLINE_TIME_NODE;

/**
 * Created by Anshul on 2/24/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private DatabaseReference dbUsers;
    public FirebaseUser myBasicDetails;
    public FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtility.with(this).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth = ((MainApplicationClass) getApplication()).getmAuth();
        myBasicDetails = ((MainApplicationClass) getApplication()).getMyDetails();

        dbUsers = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_NODE).child(myBasicDetails.getUid());

        Users users = new Users();
        users.setIs_login("true");
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(IS_LOGIN_NODE, users.getIs_login());

        dbUsers.updateChildren(childUpdate);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Users users = new Users();
        users.setIs_login("false");
        users.setLast_online_time(TimeUtils.getCurrentDateTime());
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(LAST_ONLINE_TIME_NODE, users.getLast_login_time());
        dbUsers.updateChildren(childUpdate);
    }
}
