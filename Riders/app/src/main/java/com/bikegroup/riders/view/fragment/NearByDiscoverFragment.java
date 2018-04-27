package com.bikegroup.riders.view.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.adapter.PlaceNearByListAdapter;
import com.bikegroup.riders.view.control.HeaderViewManager;
import com.bikegroup.riders.view.listener.HeaderViewClickListener;
import com.bikegroup.riders.view.model.nearbydetailmodel.Example;
import com.bikegroup.riders.view.utils.AndroidAppUtils;
import com.bikegroup.riders.view.viewModel.NearByPlaceModel;
import com.bikegroup.riders.view.webServices.PlacesInterface;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


/**
 * Created by Durgesh-Shankar on 4/14/2018.
 */

public class NearByDiscoverFragment extends Fragment {

    /**
     * View reference object
     */
    private View rootView;
    /**
     * EditText reference object for taking input from the user and the filter the list item
     * based on user input key
     */
    public static EditText etSearchFilter;
    /**
     * TextInputLayout reference object for setting the hint
     */
    private TextInputLayout layoutEtSearchFilter;
    /**
     * Recycler View reference object
     */
    private RecyclerView recyclerView;
    /**
     * Debuggable TAG
     */
    private String TAG = NearByDiscoverFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.near_by_places_list_layout, container, false);
        initFragmentView();
        getBundleData();
        manageHeaderView();
        return rootView;
    }

    private void getBundleData() {
        if (getArguments() != null) {
            if (getArguments().get("PLACE_LIST") != null) {
                ArrayList<NearByPlaceModel> mNearByPlaceModelArrayList = (ArrayList<NearByPlaceModel>) getArguments().get("PLACE_LIST");
                PlaceNearByListAdapter mPlaceNearByListAdapter = new PlaceNearByListAdapter(mNearByPlaceModelArrayList, this.getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
                recyclerView.setAdapter(mPlaceNearByListAdapter);
            }
        }
    }

    /**
     * Method Name :initFragmentView
     * Description : This method is used for initializing the the view component for fragment view
     */
    private void initFragmentView() {
        etSearchFilter = (EditText) rootView.findViewById(R.id.etSearchFilter);
        layoutEtSearchFilter = (TextInputLayout) rootView.findViewById(R.id.layoutEtSearchFilter);
        etSearchFilter.addTextChangedListener(mTextWatcher);
        etSearchFilter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (etSearchFilter.getText().toString().isEmpty()) {
                        layoutEtSearchFilter.setHint("Filter");
                        layoutEtSearchFilter.setHintEnabled(true);
                    } else {
                        layoutEtSearchFilter.setHint("Filter");
                        layoutEtSearchFilter.setHintEnabled(true);
                    }
                } else {
                    if (etSearchFilter.getText().toString().isEmpty()) {
                        layoutEtSearchFilter.setHint("No Filter");
                        layoutEtSearchFilter.setHintEnabled(true);
                    } else {
                        layoutEtSearchFilter.setHint("Filter");
                        layoutEtSearchFilter.setHintEnabled(true);
                    }
                }
            }
        });
        if (etSearchFilter.getText().toString().trim().isEmpty()) {
            etSearchFilter.clearFocus();
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    public void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeMultiTitleHeaderView(null, rootView, false, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, getActivity().getResources().getString(R.string.str_near_by_place), getActivity());
        HeaderViewManager.getInstance().setSubHeading(false, "");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, false, R.drawable.back, "");
        HeaderViewManager.getInstance().setRightSideHeaderView(false, false, android.R.drawable.ic_menu_share, "");
        HeaderViewManager.getInstance().setProgressLoader(false, true);
        HeaderViewManager.getInstance().setHeadingTextColor(true, getActivity().getResources().getColor(R.color.colorPrimary));
    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        HeaderViewClickListener headerViewClickListener = new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {

                getActivity().onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
            }
        };
        return headerViewClickListener;
    }

    /**
     * Object Name :mTextWatcher
     * Description : This is used for upating listview with filter value based on input \
     * entered by the user
     */
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        PlacesInterface service = retrofit.create(PlacesInterface.class);
        Location mLocation;
        if (MapFragment.mLocation != null) {
            mLocation = MapFragment.mLocation;
        } else {
            AndroidAppUtils.showLog(TAG, "Mlocation object is null");

        }
        int PROXIMITY_RADIUS = 5;
        Call<Example> call = service.getNearbyPlaces(type, /*mLocation.getLatitude() + "," + mLocation.getLongitude(),*/
                PROXIMITY_RADIUS, getActivity().getResources().getString(R.string.google_maps_key));

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
//                    mMap.clear();
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        AndroidAppUtils.showInfoLog(TAG, "placeName : " + placeName + "\n");
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName + " : " + vicinity);
                        // Adding Marker to the Camera.
//                        Marker m = mMap.addMarker(markerOptions);
//                        // Adding colour to the marker
//                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                        // move map camera
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }
                } catch (Exception e) {
                    AndroidAppUtils.showInfoLog(TAG, "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                AndroidAppUtils.showErrorLog("onFailure", t.toString());
            }


        });
AndroidAppUtils.showInfoLog(TAG,"Request : "+call.request());

    }

    @Override
    public void onResume() {
        super.onResume();
        build_retrofit_and_get_response("restaurant");
    }
}
