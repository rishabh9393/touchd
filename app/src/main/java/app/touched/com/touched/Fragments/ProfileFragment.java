package app.touched.com.touched.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.InterestAdapter;
import app.touched.com.touched.Adapter.Users_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.FEMALE;
import static app.touched.com.touched.Utilities.Constants.IS_OTHER;
import static app.touched.com.touched.Utilities.Constants.MALE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    User_Details profileUsersDetail;
    FirebaseAuth mAuth;
    FirebaseUser myBasicDetails;
    TextView myName, myAge, myLocation, myPosition, myCityRanking, myOverRanking;
    EditText myStatus;
    Activity context;
    RecyclerView recyclerViewInterests, recyclerViewPhotos;
    String OtherProfile;
    LinearLayout femaleLayout, maleLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle data=getArguments();
        if(data!=null) {
            OtherProfile = getArguments().getString(IS_OTHER, null);
        }
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        femaleLayout = (LinearLayout) v.findViewById(R.id.llyFemaleType);
        maleLayout = (LinearLayout) v.findViewById(R.id.llyMaleType);
        recyclerViewInterests = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerViewPhotos = (RecyclerView) v.findViewById(R.id.recycleViewGallery);
        myName = (TextView) v.findViewById(R.id.txvUserName);
        myAge = (TextView) v.findViewById(R.id.txvAge);
        myPosition = (TextView) v.findViewById(R.id.txvJob);
        myLocation = (TextView) v.findViewById(R.id.txvCity);
        myCityRanking = (TextView) v.findViewById(R.id.txvCityRanking);
        myOverRanking = (TextView) v.findViewById(R.id.txvOverallRanking);
        myStatus = (EditText) v.findViewById(R.id.txvAboutUs);
        if (OtherProfile != null) {
            if (OtherProfile.equals(MALE)) {
                maleLayout.setVisibility(View.VISIBLE);
                femaleLayout.setVisibility(View.GONE);
            } else if (OtherProfile.equals(FEMALE)) {
                femaleLayout.setVisibility(View.VISIBLE);
                maleLayout.setVisibility(View.GONE);
            }
        } else {
            maleLayout.setVisibility(View.GONE);
            femaleLayout.setVisibility(View.GONE);
        }
        addFragment();
        return v;
    }

    private void addFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, new SliderImagesFragment());
        fragmentTransaction.commit();
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
        myStatus.setText(profileUsersDetail.getAbout() != null ? profileUsersDetail.getAbout() : getResources().getString(R.string.about_us));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerViewInterests.setLayoutManager(mLayoutManager);
        recyclerViewInterests.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.Adapter mAdapterInterests = new InterestAdapter(getActivity(), new ArrayList<String>());
        recyclerViewInterests.setAdapter(mAdapterInterests);
        RecyclerView.LayoutManager mLayoutManagerGallery = new GridLayoutManager(view.getContext(), 3);

        recyclerViewPhotos.setLayoutManager(mLayoutManagerGallery);
        recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.Adapter mAdapterPhotos = new InterestAdapter(getActivity(), new ArrayList<String>());
        recyclerViewInterests.setAdapter(mAdapterPhotos);
    }


    public static Fragment newInstance(int index, Context context) {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;

    }
}
