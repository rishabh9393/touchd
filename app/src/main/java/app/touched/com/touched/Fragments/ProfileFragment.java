package app.touched.com.touched.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import app.touched.com.touched.Activites.MessagingActivity;
import app.touched.com.touched.Adapter.InterestAdapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.FEMALE;
import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;
import static app.touched.com.touched.Utilities.Constants.IS_OTHER;
import static app.touched.com.touched.Utilities.Constants.MALE;
import static app.touched.com.touched.Utilities.Constants.USERS_Details_NODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    User_Details profileUsersDetail;
    FirebaseAuth mAuth;
    FirebaseUser myBasicDetails;
    TextView myName, myAge, myLocation, myPosition, myCityRanking, myOverRanking;
    EditText myStatus;
    Activity context;
    RecyclerView recyclerViewInterests, recyclerViewPhotos;
    String OtherProfile;
    LinearLayout femaleLayout, maleLayout, interestsLayout, photosLayout;
    Bundle data;
    ImageView imvMaleMsg, imvFemaleMsg, imvMalePoke, imvFemalePoke;
    FrameLayout frameContainer;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        data = getArguments();
        if (data != null) {
            OtherProfile = data.getString(IS_OTHER, null);
        }
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        frameContainer = (FrameLayout) v.findViewById(R.id.frame_container);
        femaleLayout = (LinearLayout) v.findViewById(R.id.llyFemaleType);
        interestsLayout = (LinearLayout) v.findViewById(R.id.lly_interests);
        photosLayout = (LinearLayout) v.findViewById(R.id.lly_photos);
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
        imvFemaleMsg = (ImageView) v.findViewById(R.id.imvFemaleMsg);
        imvFemalePoke = (ImageView) v.findViewById(R.id.imvFemalePoke);
        imvMaleMsg = (ImageView) v.findViewById(R.id.imvMaleMsg);
        imvMalePoke = (ImageView) v.findViewById(R.id.imvMalePoke);
        imvMalePoke.setOnClickListener(this);
        imvMaleMsg.setOnClickListener(this);
        imvFemalePoke.setOnClickListener(this);
        imvFemaleMsg.setOnClickListener(this);
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
        frameContainer.setOnClickListener(this);

        addFragment();
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        if (OtherProfile == null) {
            myBasicDetails = ((MainApplicationClass) getActivity().getApplication()).getMyDetails();
            mAuth = ((MainApplicationClass) getActivity().getApplication()).getmAuth();
            profileUsersDetail = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        } else {
            profileUsersDetail = data.getParcelable(USERS_Details_NODE);
        }
        myName.setText(profileUsersDetail.getFirst_name() + " " + profileUsersDetail.getLast_name());
        myAge.setText(String.valueOf(profileUsersDetail.getAge()));
        myLocation.setText(profileUsersDetail.getLocation() != null ? profileUsersDetail.getLocation().getName() : "");
        myPosition.setText(profileUsersDetail.getWork() != null ? profileUsersDetail.getWork().get(0).getDescription() : "");
        myCityRanking.setText(profileUsersDetail.getRanking());
        myOverRanking.setText(profileUsersDetail.getRanking());
        myStatus.setText(profileUsersDetail.getAbout() != null ? profileUsersDetail.getAbout() : getResources().getString(R.string.about_us));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerViewInterests.setLayoutManager(mLayoutManager);
        recyclerViewInterests.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerViewInterests.setItemAnimator(new DefaultItemAnimator());
        if(profileUsersDetail.getLikes()!=null) {
            interestsLayout.setVisibility(View.VISIBLE);
            RecyclerView.Adapter mAdapterInterests = new InterestAdapter(getActivity(), profileUsersDetail.getLikes().getData());
            recyclerViewInterests.setAdapter(mAdapterInterests);
        }
        else {
            interestsLayout.setVisibility(View.GONE);
        }
        RecyclerView.LayoutManager mLayoutManagerGallery = new GridLayoutManager(view.getContext(), 3);

        recyclerViewPhotos.setLayoutManager(mLayoutManagerGallery);
        recyclerViewPhotos.setItemAnimator(new DefaultItemAnimator());
//        RecyclerView.Adapter mAdapterPhotos = new InterestAdapter(getActivity(), new ArrayList<String>());
//        recyclerViewInterests.setAdapter(mAdapterPhotos);
    }


    public static Fragment newInstance(int index, Context context) {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imvMaleMsg:
                startActivity(new Intent(getActivity(), MessagingActivity.class).putExtra(FRIENDS_TAG, profileUsersDetail));
                break;
            case R.id.imvMalePoke:
                break;
            case R.id.imvFemaleMsg:
                startActivity(new Intent(getActivity(), MessagingActivity.class).putExtra(FRIENDS_TAG, profileUsersDetail));

                break;
            case R.id.imvFemalePoke:
                break;
            case R.id.frame_container:
                int height = frameContainer.getHeight();
                if (height == 250) {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT); // or set height to any fixed value you want
                    frameContainer.setLayoutParams(lp);
                } else {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 250); // or set height to any fixed value you want
                    frameContainer.setLayoutParams(lp);
                }
                break;
        }
    }
}
