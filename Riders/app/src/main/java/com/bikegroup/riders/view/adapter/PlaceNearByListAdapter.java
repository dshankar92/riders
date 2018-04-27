package com.bikegroup.riders.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikegroup.riders.R;
import com.bikegroup.riders.databinding.NearByPlaceBinding;
import com.bikegroup.riders.view.viewModel.NearByPlaceModel;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;


/**
 * This is the custom adapter for showing the list of data logged captured by the
 * device corresponding to the beacon device index
 *
 * @author Durgesh-Shankar
 */
public class PlaceNearByListAdapter extends RecyclerView.Adapter<PlaceNearByListAdapter.ViewHolder> {

    /**
     * ArrayList reference object
     */
    private ArrayList<NearByPlaceModel> mPlaceArrayList;
    /**
     * LayoutInflater reference object
     */
    private LayoutInflater mLayoutInflater;
    /**
     * Activity reference object
     */
    private Activity mActivity;

    public PlaceNearByListAdapter(ArrayList<NearByPlaceModel> mPlaceArrayList, Activity mActivity) {
        this.mPlaceArrayList = mPlaceArrayList;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mActivity);
        }
        NearByPlaceBinding mNearByPlaceBinding =NearByPlaceBinding.inflate(mLayoutInflater,parent,false);
        return new ViewHolder(mNearByPlaceBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
NearByPlaceModel mNearByPlaceModel =mPlaceArrayList.get(position);
holder.bind(mNearByPlaceModel);
    }

    @Override
    public int getItemCount() {
        return mPlaceArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private NearByPlaceBinding mNearByPlaceBinding;

        public ViewHolder(NearByPlaceBinding mNearByPlaceBinding) {
            super(mNearByPlaceBinding.getRoot());
            this.mNearByPlaceBinding = mNearByPlaceBinding;
        }

        public void bind(NearByPlaceModel mNearByPlaceModel) {
            this.mNearByPlaceBinding.setNearByPlaceView(mNearByPlaceModel);

        }

        public NearByPlaceBinding getmNearByPlaceBinding() {
            return mNearByPlaceBinding;
        }

    }

}
