package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.touched.com.touched.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private DatabaseReference dbToRootNode;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbToRootNode = FirebaseDatabase.getInstance().getReference();

        return inflater.inflate(R.layout.fragment_message, container, false);
    }
    public static Fragment  newInstance(int index, Context context) {
        MessageFragment messageFragment=new MessageFragment();
        return messageFragment;

    }
}
