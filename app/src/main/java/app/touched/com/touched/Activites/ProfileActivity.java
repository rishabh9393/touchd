package app.touched.com.touched.Activites;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.touched.com.touched.Fragments.ProfileFragment;
import app.touched.com.touched.Fragments.SliderImagesFragment;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.FEMALE;
import static app.touched.com.touched.Utilities.Constants.IS_OTHER;
import static app.touched.com.touched.Utilities.Constants.MALE;
import static app.touched.com.touched.Utilities.Constants.USERS_Details_NODE;

public class ProfileActivity extends AppCompatActivity {
    User_Details data = new User_Details();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        data = (User_Details) getIntent().getSerializableExtra(USERS_Details_NODE);
        addFragment();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        ProfileFragment othersProfile = new ProfileFragment();
        Bundle bundle = new Bundle();
        if (data.getGender().toLowerCase().equals(MALE))
            bundle.putString(IS_OTHER, MALE);
        else
            bundle.putString(IS_OTHER, FEMALE);
        othersProfile.setArguments(bundle);
        fragmentTransaction.add(R.id.frame_container, othersProfile);
        fragmentTransaction.commit();
    }
}
