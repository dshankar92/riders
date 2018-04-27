package com.bikegroup.riders.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.adapter.DrawerItemCustomAdapter;
import com.bikegroup.riders.view.constant.GlobalConstants;
import com.bikegroup.riders.view.control.HeaderViewManager;
import com.bikegroup.riders.view.fragment.ConnectFragment;
import com.bikegroup.riders.view.fragment.FixturesFragment;
import com.bikegroup.riders.view.fragment.HomeFragment;
import com.bikegroup.riders.view.fragment.MapFragment;
import com.bikegroup.riders.view.fragment.TableFragment;
import com.bikegroup.riders.view.listener.HeaderViewClickListener;
import com.bikegroup.riders.view.model.DataModel;
import com.bikegroup.riders.view.utils.AndroidAppUtils;

import java.util.HashMap;
import java.util.Stack;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {
    /*
     *Save current tabs identifier in this..
     */
    private String mCurrentTab;
    /*
     A HashMap of stacks, where we use tab identifier as keys..
     */
    private HashMap<String, Stack<Fragment>> mStacks;
    /*
     * Fragment instance
     * */
    public static volatile Fragment currentFragment;
    /**
     * Activity reference object
     */
    public static Activity mActivity;

    /**
     * Debuggable TAG
     */
    private String TAG = HomeScreen.class.getSimpleName();

    /**
     * String arrya containing the items for navigation drawer
     */
    private String[] mNavigationDrawerItemTitles;
    /**
     * DrawerLayout reference object
     */
    private DrawerLayout mDrawerLayout;
    /**
     * ListView reference object
     */
    private ListView mDrawerList;
    /**
     * Relative layout reference object
     */
    private RelativeLayout mRelativeDrawerLayout;
    /**
     * Static instance of this class
     */
    public static HomeScreen mHomeScreenInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout);
        mHomeScreenInstance = HomeScreen.this;
        initViews();
        manageHeaderView();
        mStacks = new HashMap<>();
        mStacks.put(GlobalConstants.TAB_CONTAINER, new Stack<Fragment>());
        initializeDrawerView();
        selectItem(0);

    }

    public static HomeScreen getInstance() {

        return mHomeScreenInstance;
    }

    /**
     * Method Name : initializeDrawerView
     * Description : This method is used for initializing the view component of drawer view
     */
    private void initializeDrawerView() {
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        DataModel[] drawerItem = new DataModel[5];
        drawerItem[0] = new DataModel(R.drawable.img_profile, "Profile", R.color.white);
        drawerItem[1] = new DataModel(R.drawable.img_home, "Home", R.color.button_blue_color);
        drawerItem[2] = new DataModel(R.drawable.img_user, "User Ride", R.color.user_button_bg);
        drawerItem[3] = new DataModel(R.drawable.img_vehicle, "Vehicle Profile", R.color.barney);
        drawerItem[4] = new DataModel(R.drawable.img_rating, "Rating", R.color.rating_button_bg);
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                if (mDrawerLayout.isDrawerOpen(drawerView)) {
//                    mDrawerLayout.closeDrawer(drawerView);
//                }

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public void onClick(View v) {

    }


    /*
     * To add fragment to a tab. tag -> Tab identifier fragment -> Fragment to
     * show, false when we switch tabs, or adding first fragment to a tab true
     * when we are pushing more fragment into navigation stack. shouldAdd ->
     * Should add to fragment navigation stack (mStacks.get(tag)). false when we
     * are switching tabs (except for the first time) true in all other cases.
     */
    public void pushFragments(String tag, Fragment fragment, boolean ShouldAdd) {
        if (fragment != null && currentFragment != fragment) {
            currentFragment = fragment;
            if (tag.equals(GlobalConstants.TAB_CONTAINER)) {

                tag = GlobalConstants.TAB_CONTAINER;
            }
            mCurrentTab = tag;
            if (ShouldAdd)
                mStacks.get(tag).add(fragment);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            mFragmentTransaction.replace(R.id.content_frame, fragment).commitAllowingStateLoss();
//            if (tag.equals(GlobalConstants.TAB_CONTAINER)) {
//                ft.add(R.id.health_container, fragment);
////                activeHealthFragment();
//            }
        }

    }

    /*********************************************************************************
     * Function Name - popFragments
     * <p/>
     * Description - this function is used to remove the top fragment of a
     * specific tab on back press
     ********************************************************************************/
    private void popFragments() {
        try {
            /*  *  * Select the last fragment in current tab's stack.. which will be
             * shown after the fragment transaction given below*/

            Fragment fragment = mStacks.get(mCurrentTab).elementAt(
                    mStacks.get(mCurrentTab).size() - 1);

            // Fragment fragment = getLastElement(mStacks.get(mCurrentTab));
            /*     pop current fragment from stack.. */
            mStacks.get(mCurrentTab).remove(fragment);
            if (mStacks != null && mStacks.get(mCurrentTab) != null && !mStacks.get(mCurrentTab).isEmpty())
                currentFragment = mStacks.get(mCurrentTab).lastElement();

            /* * Remove the top fragment*/

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            // ft.add(R.id.realtabcontent, fragment);
            ft.detach(fragment);
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            AndroidAppUtils.showLog(TAG, "error pop fragment : " + e.getMessage());
        }
    }


    /**
     * initialize views
     */
    private void initViews() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mRelativeDrawerLayout = (RelativeLayout) findViewById(R.id.mRelativeDrawerLayout);
        mActivity = HomeScreen.this;

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        // making notification bar transparent
        changeStatusBarColor();


    }


    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;
        String mSelectedTab = GlobalConstants.TAB_CONTAINER;
        switch (position) {
            case 0:
                fragment = new MapFragment();
//                mSelectedTab = "MAP FRAGMENT";
                break;
            case 1:
                fragment = new HomeFragment();
//                mSelectedTab = "HOME FRAGMENT";
                break;
            case 2:
                fragment = new ConnectFragment();
//                mSelectedTab = "CONNECT FRAGMENT";
                break;
            case 3:
                fragment = new FixturesFragment();
//                mSelectedTab = "FIXTURES FRAGMENT";
                break;
            case 4:
                fragment = new TableFragment();
//                mSelectedTab = "TABLE FRAGMENT";
                break;

            default:
                break;
        }

        if (fragment != null) {
            pushFragments(mSelectedTab, fragment, true);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
//            mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//            mFragmentTransaction.replace(R.id.content_frame, fragment).commitAllowingStateLoss();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            HeaderViewManager.getInstance().setHeading(true, mNavigationDrawerItemTitles[position]);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mRelativeDrawerLayout);

        } else {
            AndroidAppUtils.showErrorLog(TAG, "Error in creating fragment");
        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    public void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeMultiTitleHeaderView(mActivity, null, false, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.str_home_screen_caption), mActivity);
        HeaderViewManager.getInstance().setSubHeading(false, "");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, false, R.drawable.drawer, "");
        HeaderViewManager.getInstance().setRightSideHeaderView(false, false, android.R.drawable.ic_menu_share, "");
        HeaderViewManager.getInstance().setProgressLoader(false, true);
        HeaderViewManager.getInstance().setHeadingTextColor(true, mActivity.getResources().getColor(R.color.colorPrimary));
    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        HeaderViewClickListener headerViewClickListener = new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {

//                mActivity.onBackPressed();
                if (mDrawerLayout != null) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }

            @Override
            public void onClickOfHeaderRightView() {
            }
        };
        return headerViewClickListener;
    }

    @Override
    public void onBackPressed() {
        popFragments();
        super.onBackPressed();
    }
}
