package com.bikegroup.riders.view.model;

/**
 * Created by Durgesh-Shankar on 4/14/2018.
 */

public class NearByPlace {
    String strPlaceName="";
    String strPlaceAddress="";

    public NearByPlace(String strPlaceName, String strPlaceAddress, String strPlaceImageUrl) {
        this.strPlaceName = strPlaceName;
        this.strPlaceAddress = strPlaceAddress;
        this.strPlaceImageUrl = strPlaceImageUrl;
    }

    public String getStrPlaceName() {
        return strPlaceName;
    }

    public void setStrPlaceName(String strPlaceName) {
        this.strPlaceName = strPlaceName;
    }

    public String getStrPlaceAddress() {
        return strPlaceAddress;
    }

    public void setStrPlaceAddress(String strPlaceAddress) {
        this.strPlaceAddress = strPlaceAddress;
    }

    public String getStrPlaceImageUrl() {
        return strPlaceImageUrl;
    }

    public void setStrPlaceImageUrl(String strPlaceImageUrl) {
        this.strPlaceImageUrl = strPlaceImageUrl;
    }

    String strPlaceImageUrl="";

}
