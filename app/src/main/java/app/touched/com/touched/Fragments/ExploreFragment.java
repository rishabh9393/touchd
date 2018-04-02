package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.SpinnerAdapter;
import app.touched.com.touched.Adapter.Users_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.IS_LOGIN_NODE;
import static app.touched.com.touched.Utilities.Constants.LOCATION_NAME;
import static app.touched.com.touched.Utilities.Constants.MSG_COUNT_NODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {
    ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int PAGE_START = 0;
    private long totalData = 0;

    private Users_Adapter mAdapter;

    private ArrayList<User_Details> full_user_details = new ArrayList<>();
    User_Details myDetails = new User_Details();
    private FirebaseAuth mAuth;
    private DatabaseReference dbToCollectExploreData;
    private ArrayList<String> filteredData = new ArrayList<>();
    GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
    String filterValue, lastKey;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int index, Context context) {
        ExploreFragment exploreFragment = new ExploreFragment();
        return exploreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        filteredData.add("Currently Online");
        filteredData.add("Nearby Location");
        filteredData.add("New Users");
        filteredData.add("Same Education Place");
        filteredData.add("Same Workplace");


        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        myDetails = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getActivity().getApplication()).getmAuth();
        //dbToCollectUserNormalData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_NODE);
        dbToCollectExploreData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE);
        mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new Users_Adapter(getActivity(), EXPLORE_FRAGMENT);

        recyclerView.setAdapter(mAdapter);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (mLayoutManager.findLastVisibleItemPosition() == totalData - 1 && !isLastPage) {
//                    isLoading = true;
//
//                    getDataFromServer();
//                }
//            }
//        });

        SpinnerAdapter dataAdapter = new SpinnerAdapter(view.getContext(), filteredData);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(spinnerItemSelectedListner);
        filterValue=filteredData.get(0);
        getCityWiseData();
    }

    public void getCityWiseData() {
        isLoading = true;
        mAdapter.addLoadingFooter();
        dbToCollectExploreData.orderByChild(LOCATION_NAME).startAt(myDetails.getLocation().getName()).endAt(myDetails.getLocation().getName()).addValueEventListener(userDetailsListner);


    }


    private AdapterView.OnItemSelectedListener spinnerItemSelectedListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            filterValue = filteredData.get(i);
            if (full_user_details.size() > 0) {
                if (mAdapter.getItemCount() > 0)
                    mAdapter.clear();
                applyFilterToData();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void applyFilterToData() {
        switch (filterValue) {
            case "Currently Online":
                mAdapter.filterByOnline(full_user_details);

                break;
            case "Nearby Location":
//                    if (myDetails.getLocation() == null) {
//                        Toast.makeText(getContext(), getResources().getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
//                    } else {
//                        dbToCollectExploreData.orderByChild(LOCATION_NAME).equalTo(myDetails.getLocation().getName()).limitToFirst(50).addValueEventListener(userDetailsListner);
//                    }
                break;
            case "New Users":
                mAdapter.filterByNewUser(full_user_details);
                break;
            case "Same Education Place":
                if (myDetails.getEducation() == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                } else {
                    mAdapter.filterByEducation(full_user_details, myDetails.getEducation().get(0).getSchool().getName());
                }
                break;
            case "Same Workplace":
                if (myDetails.getWork() == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                } else {

                    mAdapter.filterByWork(full_user_details, myDetails.getWork().getDescription());
                }
                break;
        }
    }

    private ValueEventListener userDetailsListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            totalData += dataSnapshot.getChildrenCount();
            isLoading = false;
            mAdapter.removeLoadingFooter();

            for (DataSnapshot value : dataSnapshot.getChildren()) {
                lastKey = value.getKey();
                if (!lastKey.equals(myDetails.getId())) {
                    User_Details exploreUsersDetail = value.getValue(User_Details.class);
                    full_user_details.add(exploreUsersDetail);
                } else {
                    totalData -= 1;
                }
            }

            if (dataSnapshot.getChildrenCount() < 100)
                isLastPage = true;
            applyFilterToData();
            dbToCollectExploreData.removeEventListener(userDetailsListner);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };


//    public void getDataFromServer() {
//        mAdapter.addLoadingFooter();
//
//        dbToCollectExploreData.orderByChild(LOCATION_NAME).startAt(lastKey).limitToFirst(100).addValueEventListener(userDetailsListner);
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
