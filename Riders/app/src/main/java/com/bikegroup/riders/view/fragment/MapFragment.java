package com.bikegroup.riders.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.activity.BaseActivity;
import com.bikegroup.riders.view.activity.HomeScreen;
import com.bikegroup.riders.view.constant.GlobalConstants;
import com.bikegroup.riders.view.control.HeaderViewManager;
import com.bikegroup.riders.view.listener.HeaderViewClickListener;
import com.bikegroup.riders.view.utils.AndroidAppUtils;
import com.bikegroup.riders.view.viewModel.NearByPlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.shitij.goyal.slidebutton.SwipeButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Class Name : MapFragment
 * Description : This class display google map with our current location.
 * Will update continuously on trip. while moving.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    /**
     * Google Map reference object
     */
    private GoogleMap mGoogleMap;
    /**
     * MapVIew reference object
     */
    private MapView mMapView;
    /**
     * Debuggable TAG
     */
    private String TAG = MapFragment.class.getSimpleName();
    /**
     * Location Manah=ger reference object
     */
    private LocationManager locationManager;
    /**
     * SwipeButton object reference
     */
    private SwipeButton btnSlide;
    /**
     * ImageView reference object
     */
    private ImageView mIvArrowStart, mIvArrowCenter, mIvArrowEnd;
    /**
     * Animation reference object
     */
    private Animation mSequential;
    /**
     * View reference object
     */
    private View rootView;
    /**
     * Activity reference object
     */
    private Activity mActivity;
    /**
     * Relative layout reference object
     */
    private RelativeLayout mRelativeLayoutHeader;
    /**
     * Integer constant for holding request id for place picker
     */
    private int PLACE_PICK_REQ = 101, LOC_REQ_CODE = 102;
    /**
     * Place reference object holding the detail data of place
     */
    private Place place;
    /**
     * PlaceDetectionClient reference object
     */
    protected PlaceDetectionClient placeDetectionClient;
    /**
     * ArrayList reference object
     */
    private ArrayList<NearByPlaceModel> mPlaceDetailsList;
    /**
     * FloatingActionMenu reference object
     */
    public static FloatingActionMenu actionMenu;
    public static FloatingActionButton actionButton;
    /**
     * GoogleApiClient reference object
     */
    private GoogleApiClient mGoogleApiClient;
    /**
     * Is used to get quality of service for location updates
     * from the FusedLocationProviderApi using requestLocationUpdates.
     */
    private LocationRequest mLocationRequest;
    /**
     * Marker reference object for displaying current location on map
     */
    private Marker mCurrLocationMarker;

    public static Location mLocation;

    /**
     * Empty constructor for this class
     */
    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.on_trip_map_fragment, container, false);
            mActivity = getActivity();
            manageHeaderView();
            mMapView = (MapView) rootView.findViewById(R.id.fragment_view_map);
            mMapView.onCreate(savedInstanceState);
            int intErrorCode = MapsInitializer.initialize(mActivity);
            AndroidAppUtils.showInfoLog(TAG, " Google Map error code : " + intErrorCode);
            mMapView.getMapAsync(this);
            locationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);
            btnSlide = (SwipeButton) rootView.findViewById(R.id.slide);
            mIvArrowStart = (ImageView) rootView.findViewById(R.id.imgArrowStart);
            mIvArrowCenter = (ImageView) rootView.findViewById(R.id.imgArrowCenter);
            mIvArrowEnd = (ImageView) rootView.findViewById(R.id.imgArrowEnd);
            mRelativeLayoutHeader = (RelativeLayout) rootView.findViewById(R.id.header);
            mSequential = AnimationUtils.loadAnimation(getContext(), R.anim.sequential);
            placeDetectionClient = Places.getPlaceDetectionClient(mActivity, null);
            setImageViewArrowVisibility(true);
            animateButtons();
            setTripUtilityFloatingActionMenu();
//            showPlacePicker();
            getCurrentPlaceItems();

        } catch (InflateException e) {
            AndroidAppUtils.showErrorLog(TAG, "Inflate exception");
        }
        return rootView;
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    public void manageHeaderView() {
        HeaderViewManager.getInstance().InitializeMultiTitleHeaderView(null, rootView, false, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.str_trip), mActivity);
        HeaderViewManager.getInstance().setSubHeading(false, "");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, false, R.drawable.back, "");
        HeaderViewManager.getInstance().setRightSideHeaderView(true, false, android.R.drawable.ic_menu_share, "");
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

                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.strShareCpation)));
            }
        };
        return headerViewClickListener;
    }

    /**
     * Method Name : getCurrentPlaceItems
     * Description : This method is used getting current place details and near by place.
     */
    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {

//            getCurrentPlaceData();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentPlaceData() {
        try {
            mPlaceDetailsList = new ArrayList<>();
            Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.
                    getCurrentPlace(null/*new PlaceFilter(true, mFilterList)*/);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    AndroidAppUtils.showLog(TAG, "current location places info");
                    if (task != null && task.getResult() != null) {
                        List<Place> placesList = new ArrayList<Place>();
                        PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            placesList.add(placeLikelihood.getPlace().freeze());
                            place = placeLikelihood.getPlace();
                            AndroidAppUtils.showInfoLog(TAG, "PLACE INFO : " + place.getName().toString()
                                    + "\n" + place.getAddress().toString() +
                                    "\n" + place.getLatLng().latitude +
                                    "\n " + place.getLatLng().longitude +
                                    "\n " + place.getWebsiteUri() +
                                    "\n " + place.getViewport() +
                                    "\n " + place.getAttributions()
                            );
                            NearByPlaceModel nearByPlaceModel = new NearByPlaceModel();
                            nearByPlaceModel.strPlaceName = place.getName().toString();
                            nearByPlaceModel.strPlaceAddress = place.getAddress().toString();
                            mPlaceDetailsList.add(nearByPlaceModel);
                        }
                        likelyPlaces.release();
                    }
//                PlaceNearByListAdapter recyclerViewAdapter = new
//                        PlaceNearByListAdapter(mPlaceDetailsList,
//                       mActivity);
//                .setAdapter(recyclerViewAdapter);
                }
            });
            placeResult.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AndroidAppUtils.showLog(TAG, "getCurrentPlaceData error : " + e.getMessage());
                }
            });
        } catch (Exception e) {
            AndroidAppUtils.showLog(TAG, "getCurrentPlaceData error : " + e.getMessage());
        }
    }

    /**
     * Method Name : isLocationAccessPermitted
     * Description : This method is used for checking the location permission granted or not.
     *
     * @return
     */
    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method Name : requestLocationAccessPermission
     * Description : This method is used for invoking the request for location if it is not
     * granted at run time
     */
    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

    private void showPlacePicker() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {

            startActivityForResult(intentBuilder.build(mActivity), PLACE_PICK_REQ);
        } catch (Exception e) {
            AndroidAppUtils.showErrorLog(TAG, "showPlacePicker error message :" + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOC_REQ_CODE) {
            if (resultCode == RESULT_OK) {
//                getCurrentPlaceData();
            }
        } else if (requestCode == PLACE_PICK_REQ) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(mActivity, data);
                AndroidAppUtils.showInfoLog(TAG, "PLACE INFO : " + place.getName().toString()
                        + "\n" + place.getAddress().toString() +
                        "\n" + place.getLatLng().latitude +
                        "\n " + place.getLatLng().longitude);

            }
        }
    }

    /**
     * Method Name : setImageViewArrowVisibility
     * Description : This method is used for setting the visibility of double arrow button
     *
     * @param boolIsSetVisible
     */
    private void setImageViewArrowVisibility(boolean boolIsSetVisible) {
        if (boolIsSetVisible) {
            mIvArrowCenter.setVisibility(View.VISIBLE);
            mIvArrowStart.setVisibility(View.VISIBLE);
            mIvArrowEnd.setVisibility(View.VISIBLE);

        } else {
            mIvArrowCenter.setVisibility(View.GONE);
            mIvArrowStart.setVisibility(View.GONE);
            mIvArrowEnd.setVisibility(View.GONE);
        }
    }


    /**
     * Method Name : animateButtons
     * Description : This method is used for animating the arrow button
     */
    public void animateButtons() {
        // int[] imageButtonIds = {R.id.animateButton};
        int[] imageViewIds = {R.id.imgArrowStart, R.id.imgArrowCenter, R.id.imgArrowEnd};

        int i = 1;

        for (int viewId : imageViewIds) {
            // Button imageButton = (Button) findViewById(viewId);
            Animation fadeAnimation = AnimationUtils.loadAnimation(HomeScreen.getInstance(), R.anim.fade_in);
            fadeAnimation.setStartOffset(i * 500);
            //imageButton.startAnimation(fadeAnimation);

            int imageViewId = imageViewIds[i - 1];
            ImageView textView = (ImageView) rootView.findViewById(imageViewId);
            textView.startAnimation(fadeAnimation);
            i++;
            final int intCurrentIndex = i;
            fadeAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (intCurrentIndex == 4) {
                        animateButtons();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    /**
     * Method Name : setTripUtilityFloatingActionMenu
     * Description : This method is used for setting the trip utility menu button for user to use it
     * on trip.
     * Button Discover : Discover the nearest petrol pump, hospital, restaurant or accommodation
     * Button Capture :  This button is used to click image for memories of place.
     */
    private void setTripUtilityFloatingActionMenu() {
        // Create an icon
        ImageView icon = new ImageView(mActivity);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        icon.setImageResource(R.drawable.road_trip);
        FloatingActionButton.LayoutParams mLayoutParams =
                new FloatingActionButton.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionButton = new FloatingActionButton.Builder(mActivity)
                .setContentView(icon, mLayoutParams)
                .build();

        FloatingActionButton.LayoutParams mFloatingLayoutParams = (FloatingActionButton.LayoutParams) actionButton.getLayoutParams();
        mFloatingLayoutParams.setMargins(30, 0, 30, 150);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mActivity);
        ImageView cameraImageIcon = new ImageView(mActivity);
        cameraImageIcon.setImageResource(R.drawable.camera);

        ImageView discoverImageIcon = new ImageView(mActivity);
        discoverImageIcon.setImageResource(R.drawable.discover);

        ImageView locationImageIcon = new ImageView(mActivity);
        locationImageIcon.setImageResource(R.drawable.location);

        final SubActionButton cameraImageButton = itemBuilder.setContentView(cameraImageIcon).build();
        SubActionButton discoverImageButton = itemBuilder.setContentView(discoverImageIcon).build();
        SubActionButton locationImageButton = itemBuilder.setContentView(locationImageIcon).build();
        //attach the sub buttons to the main button
        actionMenu = new FloatingActionMenu.Builder(mActivity)
                .addSubActionView(cameraImageButton, 150, 150)
                .addSubActionView(discoverImageButton, 150, 150)
                .addSubActionView(locationImageButton, 150, 150)
                .attachTo(actionButton)
                .build();

        discoverImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PLACE_LIST", mPlaceDetailsList);
                NearByDiscoverFragment mNearByDiscoverFragment = new NearByDiscoverFragment();
                mNearByDiscoverFragment.setArguments(bundle);
//                actionButton.setVisibility(View.GONE);
//                actionMenu.toggle(false);
                actionMenu.close(true);
                HomeScreen.getInstance().pushFragments(GlobalConstants.TAB_CONTAINER, mNearByDiscoverFragment, true);
            }
        });

        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionMenu.close(true);
            }
        });
        cameraImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragment mCameraFragemnt = new CameraFragment();
//                actionButton.setVisibility(View.GONE);
//                actionMenu.toggle(false);
                actionMenu.close(true);
                HomeScreen.getInstance().pushFragments(GlobalConstants.TAB_CONTAINER, mCameraFragemnt, true);
            }
        });

        btnSlide.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {
            }

            @Override
            public void onSwipeConfirm() {

                btnSlide.setActivated(false);
                btnSlide.setText("END TRIP");
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                /**
                 * Is used to enable location layer which will allow a
                 * user to interact with current user location.
                 */
                mGoogleMap.setMyLocationEnabled(true);
                AndroidAppUtils.showInfoLog(TAG, "mGoogleMap : " + mGoogleMap.getMapType());
                pinPointCurrentLocationOnMap();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        Animation mAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.sequential);
//            mRelativeLayoutHeader.setAnimation(mAnimation);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            boolean boolIsHeaderVisible;

            @Override
            public void onMapClick(LatLng latLng) {
                AndroidAppUtils.showInfoLog(TAG, "lat lon : " + boolIsHeaderVisible);
                if (!boolIsHeaderVisible) {
                    AndroidAppUtils.expand(mRelativeLayoutHeader);
                    mRelativeLayoutHeader.setVisibility(View.VISIBLE);
                    boolIsHeaderVisible = true;
                } else {
                    AndroidAppUtils.collapse(mRelativeLayoutHeader);
                    mRelativeLayoutHeader.setVisibility(View.GONE);
                    boolIsHeaderVisible = false;
                }
            }
        });

//        }


    }

    /**
     * Method Name : pinPointCurrentLocationOnMap
     * Description : this method is used for initializing and setting it on
     * the map with current location of user
     */
    public void pinPointCurrentLocationOnMap() {
        if (mMapView != null &&
                mMapView.findViewById(1) != null) {
            // Get the button view
            View locationButton = ((View) mMapView.findViewById(1).getParent()).findViewById(2);
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_TOP, btnSlide.getId());
            layoutParams.addRule(RelativeLayout.LEFT_OF, actionButton.getId());

//            locationButton.setLayoutParams(layoutParams);

        }

        /**
         * Criteria reference object for setting the accurracy , position and correct detail of the location
         */
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Location location = locationManager.getLastKnownLocation(provider);
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            LatLng myPosition = new LatLng(latitude, longitude);
            AndroidAppUtils.showInfoLog(TAG, "myPosition : " + myPosition.latitude + "-" + myPosition.longitude);
            this.mLocation = location;
            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
            if (mGoogleMap != null) {
                mGoogleMap.animateCamera(yourLocation);
                mGoogleMap.setBuildingsEnabled(true);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        pinPointCurrentLocationOnMap();


                    }

                } else {


                }
                return;
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Method Name : isGooglePlayServicesAvailable
     * Description : check if Google Play Services available or not
     *
     * @return
     */
    private boolean isGooglePlayServicesAvailable() {
        /*GoogleApiAvailability is the Helper class for verifying that
         the Google Play services APK is available and up-to-date on android device.
         If result is ConnectionResult.SUCCESS then connection was successful otherwise,
          we will return false.*/
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mActivity);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(mActivity, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    /**
     * method Name : buildGoogleApiClient
     * Description : To initialize Google Play Services.
     * We will do it using builder method
     */
    protected synchronized void buildGoogleApiClient() {
        //GoogleApiClient.Builder is used to configure client.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //A client must be connected before excecuting any operation.
        mGoogleApiClient.connect();
    }

    /**
     * This callback will have a public function onConnected()
     * which will be called whenever device is connected and disconnected.
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnectionSuspended(int i) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Provides callbacks for scenarios that result in a failed attempt to connect the
     * client to the service. Whenever connection is failed onConnectionFailed() will be called.
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * This callback will be called whenever there is change in
     * location of device. Function onLocationChanged() will be called.
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
//        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

       /* //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
    }


}
