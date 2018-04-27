package com.bikegroup.riders.view.fragment;

import android.app.Activity;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikegroup.riders.R;
import com.bikegroup.riders.databinding.HomeDataBinding;
import com.bikegroup.riders.view.adapter.HomeScreenAdapter;
import com.bikegroup.riders.view.model.HomeViewModel;
import com.bikegroup.riders.view.utils.AndroidAppUtils;
import com.bikegroup.riders.view.utils.components.FlipImageView;
import com.bikegroup.riders.view.utils.components.ViewPagerScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    /**
     * Debuggable TAG
     */
    private String TAG = HomeFragment.class.getSimpleName();
    private View rootView = null;
    private HomeDataBinding mHomeDataBinding;
    private ViewPager view_pager_rider_top, view_pager_rider_bottom;
    private HomeScreenAdapter homeScreenAdapterTop, homeScreenAdapterBottom;
    private int mDrawableLength = 0;
    private TypedArray imgs;
    /**
     * ArrayList holding the list of riders
     */
    public ArrayList<HomeViewModel> mHomeViewArrayList;
    public Handler handlerTop, handlerBottom, handlerFlipper;
    public Runnable updateTop, updateBottom, updateFlipper;
    public int currentTopPage = 0, currentBottomPage = 0;
    private FlipImageView mFlipImageViewLeft, mFlipImageViewRight;
    private Activity mActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHomeDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        rootView = mHomeDataBinding.getRoot();
        initializeView();
        setListener();
        setDummyData();
        return rootView;
    }

    private void setDummyData() {
        HomeViewModel mHomeViewModel = new HomeViewModel("24", "34", "45");
        mHomeDataBinding.setHomeViewModel(mHomeViewModel);
    }

    /**
     * Method Name : initializeView
     * Description : This method is used for initializing the view component of fragment
     */
    private void initializeView() {
        mActivity = getActivity();
        imgs = mActivity.getResources().obtainTypedArray(R.array.random_imgs);
        mFlipImageViewLeft = (FlipImageView) rootView.findViewById(R.id.mFlipImageViewLeft);
        mFlipImageViewRight = (FlipImageView) rootView.findViewById(R.id.mFlipImageViewRight);
        mFlipImageViewRight.setInterpolator(AndroidAppUtils.animationInterpolatorsObject[2]);
        mFlipImageViewLeft.setInterpolator(AndroidAppUtils.animationInterpolatorsObject[2]);
        mHomeViewArrayList = new ArrayList<>();
        mHomeViewArrayList.addAll(getArrayList("TOP RATED RIDER"));
        view_pager_rider_top = (ViewPager) rootView.findViewById(R.id.view_pager_rider_top);
        view_pager_rider_bottom = (ViewPager) rootView.findViewById(R.id.view_pager_rider_bottom);
        animateViewFlipper();
        homeScreenAdapterTop = new HomeScreenAdapter(mActivity, mHomeViewArrayList, "TOP RATED RIDER");
        view_pager_rider_top.setAdapter(homeScreenAdapterTop);
        changePagerScroller(view_pager_rider_top);
        view_pager_rider_top.setCurrentItem(0, true);
        animateTopRatedRiderPager(view_pager_rider_top);
        mHomeViewArrayList = new ArrayList<>();
        mHomeViewArrayList.addAll(getArrayList("WALL OF FAME"));
        homeScreenAdapterBottom = new HomeScreenAdapter(mActivity, mHomeViewArrayList, "WALL OF FAME");
        view_pager_rider_bottom.setAdapter(homeScreenAdapterBottom);
        changePagerScroller(view_pager_rider_bottom);
        view_pager_rider_bottom.setCurrentItem(0, true);
        animateWallOfFamePager(view_pager_rider_bottom);

    }

    /**
     * Method Name : setListener
     * Description : This method is used for setting the listener for flipview
     */
    private void setListener() {
        mFlipImageViewLeft.setOnFlipListener(new FlipImageView.OnFlipListener() {
            @Override
            public void onClick(final FlipImageView view) {
                AndroidAppUtils.showInfoLog(TAG, " onClick(FlipImageView view) : " + imgs.getDrawable(mDrawableLength));


            }

            @Override
            public void onFlipStart(FlipImageView view) {
                if (mDrawableLength < imgs.length()) {

                    mFlipImageViewLeft.setFlippedDrawable(new BitmapDrawable(getResources(),
                            AndroidAppUtils.decodeSampledBitmapFromResource(mActivity.getResources(),imgs.getResourceId(mDrawableLength,0),128,128)));
//                    Bitmap mBitmap= BitmapFactory.decodeResource(getResources(), imgs.getResourceId(mDrawableLength,0));
//                    mFlipImageViewLeft.setFlippedDrawable(new BitmapDrawable(getResources(),mBitmap));
                    if (mDrawableLength == imgs.length() - 1) {
                        mDrawableLength = 0;
                    }
                    mDrawableLength++;
                }
            }

            @Override
            public void onFlipEnd(final FlipImageView view) {
                AndroidAppUtils.showInfoLog(TAG, " onFlipEnd(FlipImageView view) : " + imgs.getDrawable(mDrawableLength) +
                        "\n mDrawableLength : " + mDrawableLength);

            }
        });
        mFlipImageViewRight.setOnFlipListener(new FlipImageView.OnFlipListener() {
            @Override
            public void onClick(final FlipImageView view) {
                AndroidAppUtils.showInfoLog(TAG, " onClick(FlipImageView view) : " + imgs.getDrawable(mDrawableLength));



            }

            @Override
            public void onFlipStart(FlipImageView view) {
                if (mDrawableLength < imgs.length()) {

//                    mFlipImageViewRight.setFlippedDrawable(new BitmapDrawable(getResources(),
//                            AndroidAppUtils.decodeSampledBitmapFromResource(mActivity.getResources(),imgs.getResourceId(mDrawableLength,0),128,128)));
                   Bitmap mBitmap= BitmapFactory.decodeResource(getResources(), imgs.getResourceId(mDrawableLength,0));
                   mFlipImageViewRight.setFlippedDrawable(new BitmapDrawable(getResources(),mBitmap));
                    if (mDrawableLength == imgs.length() - 1) {
                        mDrawableLength = 0;
                    }
                    mDrawableLength++;
                }
            }

            @Override
            public void onFlipEnd(final FlipImageView view) {
                AndroidAppUtils.showInfoLog(TAG, " onFlipEnd(FlipImageView view) : " + imgs.getDrawable(mDrawableLength) +
                        "\n mDrawableLength : " + mDrawableLength);
            }
        });
        view_pager_rider_bottom.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                AndroidAppUtils.showInfoLog("position: ", "" + position +
                        "currentTopPage : " + currentTopPage);


            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
                currentBottomPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }



        });
        view_pager_rider_top.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                AndroidAppUtils.showInfoLog("position: ", "" + position);


            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixel) {
                currentTopPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }



        });


    }

    /**
     * Method Name : animateViewFlipper
     * Description : animate images in view pager automatically
     */
    public void animateViewFlipper() {
        handlerFlipper = new Handler(Looper.getMainLooper());
        updateFlipper = new Runnable() {
            public void run() {
                mFlipImageViewLeft.toggleFlip();
                mFlipImageViewRight.toggleFlip();


            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFlipImageViewLeft.toggleFlip();
                        mFlipImageViewRight.toggleFlip();
                    }
                });

//                handlerFlipper.post(updateFlipper);
            }
        }, 500, 3000);
    }

    /**
     * Method Name : animateTopRatedRiderPager
     * Description : animate images in view pager automatically
     *
     * @param mViewPager
     */
    public void animateTopRatedRiderPager(final ViewPager mViewPager) {
        handlerTop = new Handler(Looper.getMainLooper());
        updateTop = new Runnable() {
            public void run() {
                if (currentTopPage < imgs.length()) {

                    if (currentTopPage == imgs.length() - 1) {
                        currentTopPage = -1;
                    }
                    mViewPager.setCurrentItem(currentTopPage++, true);
//
                }

            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handlerTop.post(updateTop);
            }
        }, 500, 3000);
    }

    /**
     * Method Name : animateTopRatedRiderPager
     * Description : animate images in view pager automatically
     *
     * @param mViewPager
     */
    public void animateWallOfFamePager(final ViewPager mViewPager) {
        handlerBottom = new Handler(Looper.getMainLooper());
        updateBottom = new Runnable() {
            public void run() {
                if (currentBottomPage < imgs.length()) {

                    if (currentBottomPage == imgs.length() - 1) {
                        currentBottomPage = -1;
                    }
                    mViewPager.setCurrentItem(currentBottomPage, true);
                    currentBottomPage++;
//                    mFlipImageViewRight.toggleFlip();
                }

            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handlerBottom.post(updateBottom);
            }
        }, 500, 3000);
    }

    /**
     * Method Name : changePagerScroller
     * Description : This method is used for setting the scrolling speed of view pager
     *
     * @param mViewPager
     */
    private void changePagerScroller(ViewPager mViewPager) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
            AndroidAppUtils.showErrorLog(TAG, "error of change scroller " + e);
        }
    }

    private ArrayList<HomeViewModel> getArrayList(String strTitle) {
        ArrayList<HomeViewModel> homeViewModelArrayList = new ArrayList<>();
        HomeViewModel mHomeViewModel = new HomeViewModel("34", "45", strTitle, imgs.getResourceId(0,0));
        homeViewModelArrayList.add(mHomeViewModel);
        mHomeViewModel = new HomeViewModel("34", "45", strTitle, imgs.getResourceId(1,0));
        homeViewModelArrayList.add(mHomeViewModel);
        mHomeViewModel = new HomeViewModel("34", "45", strTitle, imgs.getResourceId(2,0));
        homeViewModelArrayList.add(mHomeViewModel);
        mHomeViewModel = new HomeViewModel("34", "45", strTitle, imgs.getResourceId(3,0));
        homeViewModelArrayList.add(mHomeViewModel);
        return homeViewModelArrayList;
    }
}
