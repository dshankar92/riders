package com.bikegroup.riders.view.viewModel;

import com.bikegroup.riders.view.model.NearByPlace;

import java.io.Serializable;

/**
 * Created by Durgesh-Shankar on 4/14/2018.
 */

public class NearByPlaceModel implements Serializable {
    public String strPlaceName = "";
    public String strPlaceAddress = "";

    public NearByPlaceModel() {
    }

    public NearByPlaceModel(NearByPlace mNearByPlace) {
        this.strPlaceAddress = mNearByPlace.getStrPlaceAddress();
        this.strPlaceName = mNearByPlace.getStrPlaceName();
    }
}
