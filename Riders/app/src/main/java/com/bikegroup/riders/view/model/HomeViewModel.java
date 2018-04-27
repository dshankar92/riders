package com.bikegroup.riders.view.model;

import android.graphics.drawable.Drawable;

public class HomeViewModel {

    String mStrLike = "";
    String mStrComments = "";
    String mStrRiderType = "";


    int imgRiderImage ;

    public HomeViewModel(String mStrLike, String mStrComments, String mStrRiderType, int imgRiderImage) {
        this.mStrLike = mStrLike;
        this.mStrComments = mStrComments;
        this.mStrRiderType = mStrRiderType;
        this.imgRiderImage = imgRiderImage;
    }


    public String getStrRiderType() {
        return mStrRiderType;
    }

    public void setStrRiderType(String mStrRiderType) {
        this.mStrRiderType = mStrRiderType;
    }

    public HomeViewModel(String mStrLike, String mStrComments, String mStrRiderType) {
        this.mStrLike = mStrLike;
        this.mStrComments = mStrComments;
        this.mStrRiderType = mStrRiderType;
    }

    public String getStrLike() {
        return mStrLike;
    }

    public void setStrLike(String mStrLike) {
        this.mStrLike = mStrLike;
    }

    public String getStrComments() {
        return mStrComments;
    }

    public void setStrComments(String mStrComments) {
        this.mStrComments = mStrComments;
    }

    public int getImgRiderImage() {
        return imgRiderImage;
    }

    public void setImgRiderImage(int imgRiderImage) {
        this.imgRiderImage = imgRiderImage;
    }
}
