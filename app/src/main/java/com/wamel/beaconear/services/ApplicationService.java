package com.wamel.beaconear.services;

import com.wamel.beaconear.model.RegisteredApplication;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by mreverter on 27/3/16.
 */
public interface ApplicationService {
    @GET("/applications")
    void getApplications(Callback<List<RegisteredApplication>> callback);
}
