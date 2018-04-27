package com.bikegroup.riders.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bikegroup.riders.R;
import com.bikegroup.riders.view.fragment.TopRiderFrag;
import com.bikegroup.riders.view.model.HomeViewModel;
import com.bikegroup.riders.view.utils.AndroidAppUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Geetika on 4/16/2018.
 */

/**
 * View pager adapter
 */
public class HomeScreenAdapter extends PagerAdapter {

    private ArrayList<HomeViewModel> mHomeViewModelArrayList;
    String wall_of_fame = "";
    private LayoutInflater mLayoutInflater;
    private ImageView iv_rider;
    private TextView mTvRiderType, mTvLikes, mTvComments;
    Activity mActivity;

    public HomeScreenAdapter(Activity mActivity, ArrayList<HomeViewModel> mHomeViewModelArrayList, String wall_of_fame) {
        this.mHomeViewModelArrayList = mHomeViewModelArrayList;
        this.wall_of_fame = wall_of_fame;
        this.mActivity = mActivity;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

//    @Override
//    public Fragment getItem(int position) {
////        if(mFragmentArrayList!=null)
////        {
////            if(mFragmentArrayList.size()>position)
////            {
////                return mFragmentArrayList.get(position);
////            }else
////            {
////                TopRiderFrag mTopRiderFrag = new TopRiderFrag();
////                mFragmentArrayList.add(mTopRiderFrag);
////                return mTopRiderFrag;
////            }
////        }
//        TopRiderFrag mTopRiderFrag =new TopRiderFrag();
//        mTopRiderFrag.setTitle(wall_of_fame);
//        return mTopRiderFrag;
//    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = mLayoutInflater.inflate(R.layout.fragment_top_rider, container, false);
        initializeView(rootView);
        setDataToView(position);
        container.addView(rootView);
        return rootView;
    }

    private void setDataToView(int position) {
        if (mHomeViewModelArrayList != null && mHomeViewModelArrayList.size() > 0) {
            HomeViewModel mHomeViewModel = mHomeViewModelArrayList.get(position);
            iv_rider.setImageBitmap(
                    AndroidAppUtils.decodeSampledBitmapFromResource(mActivity.getResources()
                            ,mHomeViewModel.getImgRiderImage()
                    ,128,128));
            mTvComments.setText(mHomeViewModel.getStrComments());
            mTvLikes.setText(mHomeViewModel.getStrLike());
            mTvRiderType.setText(mHomeViewModel.getStrRiderType());
        }

    }

    private void initializeView(View rootView) {
        iv_rider = (ImageView) rootView.findViewById(R.id.iv_rider);
        mTvComments = (TextView) rootView.findViewById(R.id.mTvComments);
        mTvRiderType = (TextView) rootView.findViewById(R.id.mTvRiderType);
        mTvLikes = (TextView) rootView.findViewById(R.id.mTvLikes);
    }

    @Override
    public int getCount() {
        return mHomeViewModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
