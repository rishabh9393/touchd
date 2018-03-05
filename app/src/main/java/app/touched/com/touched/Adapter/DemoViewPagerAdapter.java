package app.touched.com.touched.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.touched.com.touched.Fragments.DemoFragment;
import app.touched.com.touched.Fragments.ExploreFragment;
import app.touched.com.touched.Fragments.LeaderBoardFragment;
import app.touched.com.touched.Fragments.MessageFragment;
import app.touched.com.touched.Fragments.ProfileFragment;

/**
 * Created by Anshul on 2/25/2018.
 */

public class DemoViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private Fragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);

            fragments.clear();
            fragments.add(ExploreFragment.newInstance(0,context));
            fragments.add(LeaderBoardFragment.newInstance(1,context));
            fragments.add(MessageFragment.newInstance(2,context));
            fragments.add(ProfileFragment.newInstance(3,context));

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        /**
         * Get the current fragment
         */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}