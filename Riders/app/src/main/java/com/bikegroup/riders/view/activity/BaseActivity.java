package com.bikegroup.riders.view.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.fragment.MapFragment;
import com.bikegroup.riders.view.fragment.NearByDiscoverFragment;
import com.bikegroup.riders.view.utils.AndroidAppUtils;

public class BaseActivity extends FragmentActivity {
    //    private Button btnInvite, btnStart;
    /**
     * Activity reference object
     */
    private Activity mActivity;
    static FragmentTransaction transaction;
    private static FragmentManager mFragmentManager = null;
    private static BaseActivity mInstance;
    /**
     * Debuggable TAG
     */
    private String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mActivity = BaseActivity.this;
        MapFragment mapFragment = new MapFragment();

        addFragment(mapFragment, "MAP FRAGMENT");


    }

    public static BaseActivity getInstance() {
        if (mInstance == null) {
            mInstance = new BaseActivity();
        }
        return mInstance;
    }

    public void addFragment(Fragment mFragment, String strTag) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                mFragmentManager = getSupportFragmentManager();
                transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.mContainerFrameLayout, mFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//                transaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            AndroidAppUtils.showLog(TAG, "addFragment error :" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {

//        for (int i = 0; i <mFragmentManager.getBackStackEntryCount() ; i++) {
//            if (mFragmentManager != null && mFragmentManager.getBackStackEntryAt(i) instanceof NearByDiscoverFragment) {
//                MapFragment.actionButton.setVisibility(View.VISIBLE);
//                MapFragment.actionMenu.toggle(true);
//            }
//        }

        super.onBackPressed();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
}
