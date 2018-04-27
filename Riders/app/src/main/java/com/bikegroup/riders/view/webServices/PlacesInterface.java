package com.bikegroup.riders.view.webServices;

import com.bikegroup.riders.view.model.nearbydetailmodel.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesInterface {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true")
    Call<Example> getNearbyPlaces(@Query("type") String type/*, @Query("location") String location*/, @Query("radius") int radius,
    @Query("key") String key);
}
