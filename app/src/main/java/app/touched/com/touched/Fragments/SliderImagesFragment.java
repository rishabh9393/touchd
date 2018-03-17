package app.touched.com.touched.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.touched.com.touched.Activites.SlidingActivity;
import app.touched.com.touched.Adapter.SlidingImage_Adapter;
import app.touched.com.touched.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderImagesFragment extends Fragment {
    private static ViewPager mPager;
    private static final Integer[] IMAGES = {R.drawable.one, R.drawable.two, R.drawable.three};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public SliderImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Collections.addAll(ImagesArray,IMAGES);
        View v=inflater.inflate(R.layout.fragment_slider_images, container, false);
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) v.findViewById(R.id.pager);


        mPager.setAdapter(new SlidingImage_Adapter(container.getContext(), ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                v.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;


        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
        return  v;
    }

}
