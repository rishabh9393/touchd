package app.touched.com.touched.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import app.touched.com.touched.Adapter.LeaderboardAdapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

import static app.touched.com.touched.Utilities.Constants.LEADERBOARD_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.MSG_COUNT_NODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoardFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    RecyclerView recyclerView;
    LeaderboardAdapter mAdapter;
    public static String ScrollType = "OverAll";
    public DatabaseReference Userdetailsnode;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    String lastKey;
    User_Details myBasicDetails;
    private boolean isLoading = false;
    TextView filter, sortby;
    ImageView filter_imv, sortby_imv;
    String location;
    private long totalData = 0;
    private boolean isLastPage = false;

     int i=0,casee;                     //remove this
     String lats,schoolMame;           //remove this

    public LeaderBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        filter = (TextView) view.findViewById(R.id.txt_FilterBy);
        sortby = (TextView) view.findViewById(R.id.txt_Sortby);
        filter_imv = (ImageView) view.findViewById(R.id.imv_Filterby);
        sortby_imv = (ImageView) view.findViewById(R.id.imv_Sortby);
        myBasicDetails = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        location = myBasicDetails.getLocation().getName();
        Userdetailsnode = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new LeaderboardAdapter(getActivity(), LEADERBOARD_FRAGMENT);
        recyclerView.setAdapter(mAdapter);
        loadIstPage();
    //   update();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastVisibleItemPosition() == totalData - 1 && !isLastPage) {
                    isLoading = true;

                    switch (ScrollType) {
                        case "CityWise":
                            Userdetailsnode.startAt(lastKey).orderByChild("location/name").equalTo(location).limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
                            break;

                        case "OverAll":
                            nextPageload();
                            break;

                        case "Age":
                            Userdetailsnode.startAt(lastKey).orderByChild("age").limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
                            break;
                        case "Location":
                            Userdetailsnode.startAt(lastKey).orderByChild("location/name").limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
                            break;

                            default:
                                nextPageload();


                    }

                }
            }
        });

        filter.setOnClickListener(this);
        sortby.setOnClickListener(this);
        filter_imv.setOnClickListener(this);
        sortby_imv.setOnClickListener(this);
        return view;
    }

    public static Fragment newInstance(int index, Context context) {
        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
        return leaderBoardFragment;

    }


    ValueEventListener getValueFromDbCallBack = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            totalData =totalData+ dataSnapshot.getChildrenCount();
            for (DataSnapshot value : dataSnapshot.getChildren()
                    ) {
                lastKey = value.getKey();
                User_Details leaderboardUsersDetail = value.getValue(User_Details.class);
                //user_details.add(exploreUsersDetail);
                mAdapter.add(leaderboardUsersDetail);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void nextPageload() {
        Userdetailsnode.startAt(lastKey).limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
    }

    private void loadIstPage() {
        Userdetailsnode.limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //  Userdetailsnode.removeEventListener(userDetailsListner);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_FilterBy:
                filtercall(view);
                break;
            case R.id.imv_Filterby:
                filtercall(view);
                break;
            case R.id.txt_Sortby:
                sortbycall(view);
                break;
            case R.id.imv_Sortby:
                sortbycall(view);
                break;
        }
    }

    public void filtercall(View view) {
        PopupMenu popup1 = new PopupMenu(getActivity(), view);
        popup1.setOnMenuItemClickListener(this);
        popup1.inflate(R.menu.filter_leaderboardmenu);
        popup1.show();

    }

    public void sortbycall(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.sortby_leaderboard_menu);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_age:
                sortbyAge();
                return true;
            case R.id.menu_location:
                sortbyLocation();
                return true;
            case R.id.menu_citywise:
                filterbyCitywise();
                return true;
            case R.id.menu_overall:
                ScrollType="OverAll";
                mAdapter.clear();
                loadIstPage();
                return true;
            default:
                return false;
        }
    }

    public void filterbyCitywise() {
        ScrollType = "CityWise";
         mAdapter.clear();
        Userdetailsnode.orderByChild("location/name").equalTo(location).limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
    }

    public void sortbyAge() {
        ScrollType = "Age";
        mAdapter.clear();
        Userdetailsnode.orderByChild("age").limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
    }

    public void sortbyLocation(){
        ScrollType = "Location";
        mAdapter.clear();
        Userdetailsnode.orderByChild("location/name").limitToFirst(50).addValueEventListener(getValueFromDbCallBack);
    }

    private class NearbyLocation extends AsyncTask<Intent, String, String> {
        private int response;
        Bitmap bitmap = null;
        String picturePath, encoded, path;
        ProgressDialog pDialog;

        @Override
        protected String doInBackground(Intent... params) {


            return path;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }
    }
    public void update(){
        final ArrayList names =new ArrayList();
        names.add(40);
        names.add(26);
        names.add(30);
        names.add(25);
        names.add(29);
        names.add(18);
        names.add(20);
        names.add(21);
        names.add(23);
        names.add(56);

        final ArrayList last =new ArrayList();
        last.add("-74.00594130000002");
        last.add("77.08954210000002");
        last.add("77.03296649999993");
        last.add("77.03321299999993");
        last.add("77.02013839999995");
        last.add("77.07113270000002");
        last.add("77.05356800000004");
        last.add("77.1810408");
        last.add("77.18545589999997");
        last.add("77.24102029999995");

        final ArrayList schoolname =new ArrayList();
        schoolname.add("St.High School");
        schoolname.add("Sample Secondary School");
        schoolname.add("Xavier High School");
        schoolname.add("Shjhk Secondary School");
        schoolname.add("Aisha High School");
        schoolname.add("Ahhsd Secondary School");
        schoolname.add("Ajkb High School");
        schoolname.add("Ajdns Secondary School");
        schoolname.add("Ahjsh High School");
        schoolname.add("Secondary School");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child("user_details").orderByChild("age");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()
                        ) {
                    if(i==names.size()){
                        i=0;

                    }
                        casee = (int) names.get(i);
                      //  lats =(String)last.get(i);
//                    schoolMame =(String)schoolname.get(i);
                   i++;
//                    ArrayList<User_Details.Education> list=new ArrayList<>();
//
//                    User_Details.Education data= new User_Details.Education();
//                    data.setId(casee);
//                    data.setType("School");
//                    User_Details.School school=new User_Details.School();
//                    school.setId(casee);
//                    school.setName(schoolMame);
//                    data.setSchool(school);
//                    list.add(data);
//                    User_Details.Education data1= new User_Details.Education();
//                    data1.setId(casee);
//                    data1.setType("School");
//                    User_Details.School school1=new User_Details.School();
//                    school1.setId(casee);
//                    school1.setName(schoolMame);
//                    data1.setSchool(school1);
//                    list.add(data1);
//                    User_Details.Location data = new User_Details.Location();
//                    data.setLatitude();
//                    data.setLongitude();

                    String key = dataSnapshot1.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                    String path = "/" + dataSnapshot.getKey() + "/" + key;

                //    String key = dataSnapshot1.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                 //   String path = "/" + key + "/";
                    String value2 = "/" + dataSnapshot.getKey() + "/" + key+"/"+"location";;
                    HashMap<String, Object> result = new HashMap<>();
                   result.put("age",casee);
                //   result.put("school",lats);
                   result.put(MSG_COUNT_NODE,String.valueOf(new Random().nextInt(5)));
                    HashMap<String, Object> result2 = new HashMap<>();
                    result2.put("latitude", casee);
                    result2.put("longitude",lats);
                  //  result.put("type",lats);
                  //  reference.child(path).removeValue();
                 //   reference.child(value2).removeValue();
                    reference.child(path).updateChildren(result);
                 //  reference.child(value2).updateChildren(result2);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);

            }
        });
    }

}