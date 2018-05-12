package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.MessageSentAdapter;
import app.touched.com.touched.Adapter.Users_Adapter;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.MessageSentModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private DatabaseReference dbToRootNode, dbToMyTimeline;
    private User_Details myDetails;
    private RecyclerView recyclerView;
    private MessageSentAdapter mAdapter;
    private ArrayList<MessageSentModel> myTimelineList;
    private RelativeLayout noDataFoundHeart;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        noDataFoundHeart = (RelativeLayout) view.findViewById(R.id.rlv_noDataHeart);
        myDetails = ((MainApplicationClass) getActivity().getApplication()).getProfileUsersDetail();
        myTimelineList = new ArrayList<>();
        dbToRootNode = FirebaseDatabase.getInstance().getReference();
        dbToMyTimeline = dbToRootNode.child(getString(R.string.MYMESSAGESTAG)).child(myDetails.getKey());
        dbToMyTimeline.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        noDataFoundHeart.setVisibility(View.VISIBLE);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        mAdapter = new MessageSentAdapter(getActivity(), myTimelineList);
        recyclerView.setAdapter(mAdapter);

        dbToMyTimeline.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {

                    MessageSentModel newData = value.getValue(MessageSentModel.class);
                    if (myTimelineList.contains(newData))
                        myTimelineList.remove(newData);
                    myTimelineList.add(0, newData);
                    mAdapter.notifyItemInserted(0);

                }
                if (myTimelineList.size() > 0)
                    noDataFoundHeart.setVisibility(View.GONE);
                else
                    noDataFoundHeart.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static Fragment newInstance(int index, Context context) {
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;

    }
}
