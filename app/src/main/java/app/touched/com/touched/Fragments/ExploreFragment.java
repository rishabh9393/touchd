package app.touched.com.touched.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import static app.touched.com.touched.Utilities.Constants.IS_ONLINE;
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
    private LinearLayout llyNoDataFound;
    private Users_Adapter mAdapter;
    FirebaseUser currentFUser;
    private ArrayList<User_Details> full_user_details = new ArrayList<>();
    User_Details myDetails = new User_Details();
    private FirebaseAuth mAuth;
    private DatabaseReference dbToCollectExploreData;
    private ArrayList<String> filteredData = new ArrayList<>();
    GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
    String filterValue, lastKey;
    RecyclerView recyclerView;
    pl.droidsonroids.gif.GifImageView loadingNearBy;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        llyNoDataFound = (LinearLayout) view.findViewById(R.id.msg_noDataFound);
        loadingNearBy = (pl.droidsonroids.gif.GifImageView) view.findViewById(R.id.loadingNearBy);
        myDetails = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        mAuth = ((MainApplicationClass) getActivity().getApplication()).getmAuth();
        //dbToCollectUserNormalData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_NODE);
        dbToCollectExploreData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE);
        currentFUser = ((MainApplicationClass) getActivity().getApplication()).getMyDetails();
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
        filterValue = filteredData.get(0);
        getCityWiseData();
    }

    /***
     * get the data from Firebase on the location basis
     */
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

    private void noDataFoundShow() {
        recyclerView.setVisibility(View.GONE);
        llyNoDataFound.setVisibility(View.VISIBLE);
        loadingNearBy.setVisibility(View.GONE);

    }

    private void noDataFoundHide() {
        recyclerView.setVisibility(View.VISIBLE);
        llyNoDataFound.setVisibility(View.GONE);
        loadingNearBy.setVisibility(View.GONE);

    }

    private void findingNearbyLocationShow() {
        recyclerView.setVisibility(View.GONE);
        llyNoDataFound.setVisibility(View.GONE);
        loadingNearBy.setVisibility(View.VISIBLE);

    }

    private void findingNearbyLocationHide() {
        recyclerView.setVisibility(View.VISIBLE);
        llyNoDataFound.setVisibility(View.GONE);
        loadingNearBy.setVisibility(View.GONE);

    }

    private void applyFilterToData() {
        if (full_user_details.size() > 0) {
            noDataFoundHide();
            switch (filterValue) {
                case "Currently Online":
                    ArrayList<User_Details> filterList = new ArrayList<>();
                    for (User_Details data : full_user_details) {
                        if (data.getIs_login().equals(IS_ONLINE)) {
                            filterList.add(data);

                        }
                    }
                    if (filterList.size() > 0)
                        mAdapter.filterByOnline(filterList);
                    else
                        noDataFoundShow();

                    break;
                case "Nearby Location":
                    if (myDetails.getLocation() == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
                    } else {
                        findingNearbyLocationShow();
                        dbToCollectExploreData.removeEventListener(userDetailsListner);

                        new NearbyLocation().execute();

                    }
                    break;
                case "New Users":
                    dbToCollectExploreData.removeEventListener(userDetailsListner);

                    mAdapter.filterByNewUser(full_user_details);
                    break;
                case "Same Education Place":
                    if (myDetails.getEducation() == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                    } else {
                        dbToCollectExploreData.removeEventListener(userDetailsListner);

                        mAdapter.filterByEducation(full_user_details, myDetails.getEducation().get(0).getSchool().getName());
                    }
                    break;
                case "Same Workplace":
                    if (myDetails.getWork() == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                    } else {
                        dbToCollectExploreData.removeEventListener(userDetailsListner);

                        mAdapter.filterByWork(full_user_details, myDetails.getWork().getDescription());
                    }
                    break;
            }
        } else {
            llyNoDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
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
                if (!lastKey.equals(currentFUser.getUid())) {// later on change this fire id with user id
                    User_Details exploreUsersDetail = value.getValue(User_Details.class);
                    full_user_details.add(exploreUsersDetail);
                } else {
                    totalData -= 1;
                }
            }
            if (dataSnapshot.getChildrenCount() < 100)
                isLastPage = true;

            applyFilterToData();

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
    public class NearbyLocation extends AsyncTask<Intent, String, Void> {
        private int response;
        Bitmap bitmap = null;
        String picturePath, encoded, path;
        ProgressDialog pDialog;


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected Void doInBackground(Intent... intents) {
            for (User_Details user : full_user_details
                    ) {
                float[] results = new float[1];
                Location.distanceBetween(Double.parseDouble(myDetails.getLocation().getLatitude()), Double.parseDouble(myDetails.getLocation().getLongitude()),
                        Double.parseDouble(user.getLocation().getLatitude()), Double.parseDouble(user.getLocation().getLongitude()), results);
                float distance = results[0];
                user.getLocation().setDistance(distance);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            findingNearbyLocationHide();
            mAdapter.filterByLocation(full_user_details);
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbToCollectExploreData.removeEventListener(userDetailsListner);

    }
}
