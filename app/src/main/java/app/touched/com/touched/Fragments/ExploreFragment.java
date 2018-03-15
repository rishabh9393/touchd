package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.touched.com.touched.Adapter.Users_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

import static android.R.layout.browser_link_context_header;
import static android.R.layout.simple_spinner_item;
import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    private Users_Adapter mAdapter;
    private ArrayList<User_Details> user_details = new ArrayList<>();
    User_Details myDetails = new User_Details();
    private FirebaseAuth mAuth;
    private DatabaseReference dbToCollectExploreData, dbToCollectUserNormalData;
    private List<String> filteredData = new ArrayList<>();

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
        dbToCollectUserNormalData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_NODE);
        dbToCollectExploreData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE);
        dbToCollectExploreData.orderByChild("location/name").equalTo("New Delhi, India").limitToFirst(50).addValueEventListener(userDetailsListner);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new Users_Adapter(getActivity(), user_details, EXPLORE_FRAGMENT);
        recyclerView.setAdapter(mAdapter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), simple_spinner_item, filteredData);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String filterValue = adapterView.getItemAtPosition(i).toString();
                dbToCollectExploreData.removeEventListener(userDetailsListner);

                switch (filterValue) {
                    case "Currently Online":
                        dbToCollectUserNormalData.orderByChild("/is_login").equalTo("true").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot value : dataSnapshot.getChildren()) {
                                    dbToCollectExploreData.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User_Details exploreUsersDetail = dataSnapshot.getValue(User_Details.class);
                                            user_details.add(exploreUsersDetail);
                                            mAdapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;
                    case "Nearby Location":
                        if (myDetails.getLocation() == null) {
                            Toast.makeText(getContext(), getResources().getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
                        } else {
                            dbToCollectExploreData.orderByChild("location/name").equalTo(myDetails.getLocation().getName()).limitToFirst(50).addValueEventListener(userDetailsListner);
                        }
                        break;
                    case "New Users":
                        dbToCollectExploreData.orderByChild("msg_count").equalTo("1").limitToFirst(50).addValueEventListener(userDetailsListner);
// need to change
                        break;
                    case "Same Education Place":
                        if (myDetails.getEducation() == null) {
                            Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                        } else {
//will only show default first school value
                            dbToCollectExploreData.orderByChild("education/0/school/name").equalTo(myDetails.getEducation().get(0).getSchool().getName()).limitToFirst(50).addValueEventListener(userDetailsListner);
                        }
                        break;
                    case "Same Workplace":
                        if (myDetails.getWork() == null) {
                            Toast.makeText(getContext(), getResources().getString(R.string.education_not_found), Toast.LENGTH_LONG).show();

                        } else {
//will only show default first work value
                            dbToCollectExploreData.orderByChild("work/description").equalTo(myDetails.getWork().getDescription()).limitToFirst(50).addValueEventListener(userDetailsListner);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private ValueEventListener userDetailsListner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot value : dataSnapshot.getChildren()
                    ) {
                User_Details exploreUsersDetail = value.getValue(User_Details.class);
                user_details.add(exploreUsersDetail);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbToCollectExploreData.removeEventListener(userDetailsListner);
    }
}
