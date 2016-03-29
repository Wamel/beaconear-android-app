package com.wamel.beaconear.services;
import com.wamel.beaconear.model.Type;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by mreverter on 26/3/16.
 */
public interface BeaconTypesService {
    @GET("/beacon/{id}/types")
    void getTypesForBeacon(@Path(value = "id", encode = false) String beaconId, Callback<List<Type>> callback);

    @POST("/beacon/{id}/types")
    void createBeaconWithTypes(@Path(value = "id", encode = false) String beaconId, @Body List<Type> types, Callback<Type> callback);
}
