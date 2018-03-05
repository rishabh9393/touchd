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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.Users_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {

    private Users_Adapter mAdapter;
    private ArrayList<User_Details> user_details = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference dbToCollectExploreData;

    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        mAuth = ((MainApplicationClass) getActivity().getApplication()).getmAuth();
        dbToCollectExploreData = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_Details_NODE);
        dbToCollectExploreData.addValueEventListener(userDetailsListner);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new Users_Adapter(getActivity(), user_details, EXPLORE_FRAGMENT);
        recyclerView.setAdapter(mAdapter);
    }
  public static Fragment  newInstance(int index, Context context) {
        ExploreFragment exploreFragment=new ExploreFragment();
        return exploreFragment;

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
