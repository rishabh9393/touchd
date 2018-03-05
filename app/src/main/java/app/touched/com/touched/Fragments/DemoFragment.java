package app.touched.com.touched.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment {
    private  Context context;


    public DemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_demo, container, false);

        return v;
    }
    public static DemoFragment newInstance(int index,Context context) {

        DemoFragment fragment = new DemoFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
       // Toast.makeText(context,index,Toast.LENGTH_SHORT).show();
        Log.e("ff",String.valueOf(index));
        return fragment;
    }

}
