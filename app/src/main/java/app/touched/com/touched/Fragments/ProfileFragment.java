package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    User_Details profileUsersDetail;
    FirebaseAuth mAuth;
    FirebaseUser myBasicDetails;
    TextView myName, myAge, myLocation, myPosition, myCityRanking, myOverRanking;
    EditText myStatus;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        myName = (TextView) v.findViewById(R.id.txvUserName);
        myAge = (TextView) v.findViewById(R.id.txvAge);
        myPosition = (TextView) v.findViewById(R.id.txvJob);
        myLocation = (TextView) v.findViewById(R.id.txvCity);
        myCityRanking = (TextView) v.findViewById(R.id.txvCityRanking);
        myOverRanking = (TextView) v.findViewById(R.id.txvOverallRanking);
        myStatus = (EditText) v.findViewById(R.id.txvAboutUs);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myBasicDetails = ((MainApplicationClass) getActivity().getApplication()).getMyDetails();
        mAuth = ((MainApplicationClass) getActivity().getApplication()).getmAuth();
        profileUsersDetail = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        myName.setText(profileUsersDetail.getFirst_name() + " " + profileUsersDetail.getLast_name());
        myAge.setText(profileUsersDetail.getAge());
        myLocation.setText(profileUsersDetail.getLocation() != null ? profileUsersDetail.getLocation().getName() : "");
        myPosition.setText(profileUsersDetail.getWork() != null ? profileUsersDetail.getWork().getDescription() : "");
        myCityRanking.setText(profileUsersDetail.getRanking());
        myOverRanking.setText(profileUsersDetail.getRanking());
        myStatus.setText(profileUsersDetail.getAbout() != null ? profileUsersDetail.getAbout() : "");

    }


    public static Fragment newInstance(int index, Context context) {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;

    }
}
