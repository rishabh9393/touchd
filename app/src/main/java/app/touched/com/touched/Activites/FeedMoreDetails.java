package app.touched.com.touched.Activites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

public class FeedMoreDetails extends BaseActivity implements View.OnClickListener {
    LinearLayout dynamicWorkLayout, dynamicEducationLayout;
    TextView addwork, addEdu;
    private DatabaseReference dbToMyNode;
    private User_Details myDetails = new User_Details();
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_more_details);
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();

        dbToMyNode = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE).child(myDetails.getKey());
        save = (Button) findViewById(R.id.btn_save);
        dynamicWorkLayout = (LinearLayout) findViewById(R.id.dynamicWorkLayout);
        dynamicEducationLayout = (LinearLayout) findViewById(R.id.dynamicEducationLayout);
        addwork = (TextView) findViewById(R.id.imv_addWork);
        addEdu = (TextView) findViewById(R.id.imv_addEducation);


        addwork.setOnClickListener(this);
        addEdu.setOnClickListener(this);
        save.setOnClickListener(this);
        if (myDetails.getWork() != null) {
            for (User_Details.Work work : myDetails.getWork()) {
                addWorkLayout(work);
            }
        }
        else {
            addWorkLayout(null);
        }
        if (myDetails.getEducation() != null) {
            for (User_Details.Education education : myDetails.getEducation()) {

                addEducationLayout(education);
            }
        }
        else {
            addEducationLayout(null);
        }
    }

    private void addWorkLayout(User_Details.Work work) {
        View child = getLayoutInflater().inflate(R.layout.add_more_fields_layout, null);
        LinearLayout p_layout = (LinearLayout) child;
        LinearLayout c_layout = (LinearLayout) p_layout.getChildAt(0);
        LinearLayout cc_layout = (LinearLayout) c_layout.getChildAt(0);
        EditText addNewItem = (EditText) cc_layout.getChildAt(1);
        addNewItem.setHint("Add your current work here");
        if (work != null)
            addNewItem.setText(work.getDescription());
        //  for (int i = 0; i < layout.getChildCount(); i++)
        dynamicWorkLayout.addView(child);
    }

    private void addEducationLayout(User_Details.Education education) {
        View child = getLayoutInflater().inflate(R.layout.add_more_fields_layout, null);
        LinearLayout p_layout = (LinearLayout) child;
        LinearLayout c_layout = (LinearLayout) p_layout.getChildAt(0);
        LinearLayout cc_layout = (LinearLayout) c_layout.getChildAt(0);
        EditText addNewItem = (EditText) cc_layout.getChildAt(1);
        addNewItem.setHint("Add your current education here");
        if (education != null)
            addNewItem.setText(education.getSchool().getName());
        dynamicEducationLayout.addView(child);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_addWork:
                addWorkLayout(null);
                break;
            case R.id.imv_addEducation:
                addEducationLayout(null);
                break;
            case R.id.btn_save:
                saveUserData();
            default:
                break;
        }
    }

    private void saveUserData() {
        int totalWorkLayout = dynamicWorkLayout.getChildCount();
        int totalEducationLayout = dynamicEducationLayout.getChildCount();
        List<User_Details.Work> myWork = new ArrayList<User_Details.Work>();
        List<User_Details.Education> myEdu = new ArrayList<User_Details.Education>();
        for (int i = 0; i < totalWorkLayout; i++) {
            View child = dynamicWorkLayout.getChildAt(i);
            LinearLayout p_layout = (LinearLayout) child;
            LinearLayout c_layout = (LinearLayout) p_layout.getChildAt(0);
            LinearLayout cc_layout = (LinearLayout) c_layout.getChildAt(0);
            EditText getworkItem = (EditText) cc_layout.getChildAt(1);
            if (getworkItem.getText().toString().isEmpty() && i == 0) {
                getworkItem.setError("Please enter the value");
            } else {
                if (!getworkItem.getText().toString().isEmpty()) {
                    User_Details.Work data = new User_Details.Work();
                    data.setDescription(getworkItem.getText().toString());
                    myWork.add(data);
                }
            }

        }
        for (int i = 0; i < totalEducationLayout; i++) {
            View child = dynamicEducationLayout.getChildAt(i);
            LinearLayout p_layout = (LinearLayout) child;
            LinearLayout c_layout = (LinearLayout) p_layout.getChildAt(0);
            LinearLayout cc_layout = (LinearLayout) c_layout.getChildAt(0);
            EditText getworkItem = (EditText) cc_layout.getChildAt(1);
            if (getworkItem.getText().toString().isEmpty() && i == 0) {
                getworkItem.setError("Please enter the value");
            } else {
                if (!getworkItem.getText().toString().isEmpty()) {
                    User_Details.Education data = new User_Details.Education();
                    User_Details.School school = new User_Details.School();
                    school.setName(getworkItem.getText().toString());
                    data.setSchool(school);
                    myEdu.add(data);
                }
            }

        }

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("education", myEdu);
        childUpdate.put("work", myWork);
        dbToMyNode.updateChildren(childUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(FeedMoreDetails.this, MainActivity.class));

            }
        });
    }
}
