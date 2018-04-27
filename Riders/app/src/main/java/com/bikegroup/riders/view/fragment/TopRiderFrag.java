package com.bikegroup.riders.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.model.DataModel;
import com.bikegroup.riders.view.model.HomeViewModel;

/**
 * Created by Geetika on 4/16/2018.
 */
public class TopRiderFrag extends Fragment {

    static ImageView iv_rider;
    static View rootView;
    static  TextView mTvRiderType, mTvLikes, mTvComments;
    private LinearLayout mFlipIVLikeComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_top_rider, container, false);
        iv_rider = (ImageView) rootView.findViewById(R.id.iv_rider);
        /**
         * LIKES AND COMMENTS UPDATE
         */
        mTvLikes = (TextView) rootView.findViewById(R.id.mTvLikes);
        mTvRiderType = (TextView) rootView.findViewById(R.id.mTvRiderType);
        mTvComments = (TextView) rootView.findViewById(R.id.mTvComments);
        return rootView;
    }

    public static TopRiderFrag fetchImg(String imgUrl) {

        TopRiderFrag f = new TopRiderFrag();
        Bundle b = new Bundle();
        b.putString("img", imgUrl);
        f.setArguments(b);
        return f;
    }

    public static ImageView getImageView() {
        if (iv_rider == null) {
            iv_rider = (ImageView) rootView.findViewById(R.id.iv_rider);
        }
        return iv_rider;
    }

    public static void setData(HomeViewModel model) {
        mTvRiderType.setText(model.getStrRiderType());
        mTvLikes.setText(model.getStrLike());
        mTvComments.setText(model.getStrComments());
    }

    public static void setTitle(String strTitle) {
        if (mTvRiderType != null)
            mTvRiderType.setText(strTitle);
    }
}
