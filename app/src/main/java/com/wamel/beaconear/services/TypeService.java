package com.wamel.beaconear.services;

import com.wamel.beaconear.model.AttributeType;
import com.wamel.beaconear.model.Type;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by mreverter on 26/3/16.
 */
public interface TypeService {
    @POST("/types")
    void createType(@Body Type type, Callback<Type> callback);
    
    @GET("/attribute_types")
    void getAttributeTypes(Callback<List<AttributeType>> callback);

}
