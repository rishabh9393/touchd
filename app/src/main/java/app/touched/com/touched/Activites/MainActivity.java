package app.touched.com.touched.Activites;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.touched.com.touched.Adapter.DemoViewPagerAdapter;
import app.touched.com.touched.Fragments.DemoFragment;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.Constants;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {


    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FrameLayout mContentFrame;
    private int mCurrentSelectedPosition;
    private AHBottomNavigationViewPager viewPager;
    AHBottomNavigation bottomNavigation;
    private AHBottomNavigationAdapter navigationAdapter;
    private Fragment currentFragment;
    private DemoViewPagerAdapter adapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private CircleImageView profileImage;
    private TextView userName;
    private User_Details myDetails = new User_Details();
    public static int navItemIndex = 0;
    View navHeader;

    // private boolean useMenuResource = true;
    // private int[] tabColors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        boolean enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
//                .getBoolean("translucentNavigation", false);
//        setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);

        setContentView(R.layout.activity_main);
        //  Toast.makeText(this, "welcome " + myBasicDetails.getDisplayName(), Toast.LENGTH_SHORT).show();
        myDetails = ((MainApplicationClass) getApplication()).getProfileUsersDetail();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setUpToolbar();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);
        navHeader = mNavigationView.getHeaderView(0);
        profileImage = (CircleImageView) navHeader.findViewById(R.id.imv_Profile);
        userName = (TextView) navHeader.findViewById(R.id.txvName);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        setUpNavDrawer();

// Create items
//        if (useMenuResource) {
//            tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
//            navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
//            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
//        } else {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.explore), R.drawable.explore, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.leaderboard), R.drawable.leaderboard, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.message), R.drawable.message, R.color.color_tab_3);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getResources().getString(R.string.profile), R.drawable.person, R.color.color_tab_4);

// Add items
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);
        bottomNavigation.addItems(bottomNavigationItems);
        // }
        //bottomNavigation.addItem(item4);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
        bottomNavigation.setTranslucentNavigationEnabled(false);
        bottomNavigation.setColored(true);
        bottomNavigation.setBackgroundColor(Color.BLACK);
        bottomNavigation.setDefaultBackgroundColor(Color.BLACK);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int a;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        bottomNavigation.setAccentColor(Color.BLUE);
//        bottomNavigation.setInactiveColor(Color.DKGRAY);
        viewPager.setOffscreenPageLimit(4);
        adapter = new DemoViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);

        currentFragment = adapter.getCurrentFragment();


        //bottomNavigation.setCurrentItem(1);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {


            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (wasSelected) {
                    //currentFragment.refresh();
                    return true;
                }

                if (currentFragment != null) {
                    //  currentFragment.willBeHidden();
                }

                viewPager.setCurrentItem(position, false);

                if (currentFragment == null) {
                    return true;
                }

                currentFragment = adapter.getCurrentFragment();
                // currentFragment.willBeDisplayed();

                if (position == 1) {
                    bottomNavigation.setNotification("", 1);

//                    floatingActionButton.setVisibility(View.VISIBLE);
//                    floatingActionButton.setAlpha(0f);
//                    floatingActionButton.setScaleX(0f);
//                    floatingActionButton.setScaleY(0f);
//                    floatingActionButton.animate()
//                            .alpha(1)
//                            .scaleX(1)
//                            .scaleY(1)
//                            .setDuration(300)
//                            .setInterpolator(new OvershootInterpolator())
//                            .setListener(new Animator.AnimatorListener() {
//                                @Override
//                                public void onAnimationStart(Animator animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    floatingActionButton.animate()
//                                            .setInterpolator(new LinearOutSlowInInterpolator())
//                                            .start();
//                                }
//
//                                @Override
//                                public void onAnimationCancel(Animator animation) {
//
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animator animation) {
//
//                                }
//                            })
//                            .start();

                } else {
//                    if (floatingActionButton.getVisibility() == View.VISIBLE) {
//                        floatingActionButton.animate()
//                                .alpha(0)
//                                .scaleX(0)
//                                .scaleY(0)
//                                .setDuration(300)
//                                .setInterpolator(new LinearOutSlowInInterpolator())
//                                .setListener(new Animator.AnimatorListener() {
//                                    @Override
//                                    public void onAnimationStart(Animator animation) {
//
//                                    }
//
//                                    @Override
//                                    public void onAnimationEnd(Animator animation) {
//                                        floatingActionButton.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onAnimationCancel(Animator animation) {
//                                        floatingActionButton.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onAnimationRepeat(Animator animation) {
//
//                                    }
//                                })
//                                .start();
//                    }
                }

                return true;
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        navItemIndex = 0;
                        break;

                }
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_item_1:
//                        Snackbar.make(mContentFrame, "Item One",
//                                Snackbar.LENGTH_SHORT).show();
//                        mCurrentSelectedPosition = 0;
//                        menuItem.setChecked(true);
//                        // Set action bar title
//                        setTitle(menuItem.getTitle());
//                        // Close the navigation drawer
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    case R.id.navigation_item_2:
//                        Snackbar.make(mContentFrame, "Item Two",
//                                Snackbar.LENGTH_SHORT).show();
//                        mCurrentSelectedPosition = 1;
//                        menuItem.setChecked(true);
//                        // Set action bar title
//                        setTitle(menuItem.getTitle());
//                        // Close the navigation drawer
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    default:
                return true;
//                }
            }
        });

    }


    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.drawer_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.navigation_item_1:
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setUpNavDrawer() {
        Picasso.with(this).load(myDetails.getPicture().getData().getUrl()).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(profileImage);
        userName.setText(myDetails.getFirst_name() + " " + myDetails.getLast_name());
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
//        if (mToolbar != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            mToolbar.setNavigationIcon(R.mipmap.ic_launcher);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                }
//            });
//        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
    }
}


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "app.touched.com.touched",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

