package com.wamel.beaconear.core;

import android.content.Context;

import com.wamel.beaconear.model.AttributeType;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.Type;
import com.wamel.beaconear.services.ApplicationService;
import com.wamel.beaconear.services.BeaconTypesService;
import com.wamel.beaconear.services.TypeService;
import com.wamel.beaconear.utils.HttpClientUtil;
import com.wamel.beaconear.utils.JsonUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by mreverter on 26/3/16.
 */
public class BeaconearAPI {

    private final String ENDPOINT = "http://wamel.io:8089/";

    private String mAccessToken;
    private Context mContext;
    private RestAdapter mRestAdapter;

    private BeaconearAPI(BeaconearAPI.Builder builder) {
        mAccessToken = builder.getApiKey();
        mContext = builder.getContext();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(JsonUtil.getInstance().getGson()))
                .setClient(HttpClientUtil.getClient(mContext))
                .build();
    }

    public void getApplications(Callback<List<RegisteredApplication>> callback) {
        ApplicationService applicationService = mRestAdapter.create(ApplicationService.class);
        applicationService.getApplications(callback);
    }

    public void getAttributeTypes(Callback<List<AttributeType>> callback) {
        TypeService typeService = mRestAdapter.create(TypeService.class);
        typeService.getAttributeTypes(callback);
    }


    public void createType(Type newType, Callback<Type> callback) {
        TypeService typeService = mRestAdapter.create(TypeService.class);
        typeService.createType(newType, callback);
    }

    public void getTypesForBeacon(String beaconId, Callback<List<Type>> callback) {
        BeaconTypesService beaconTypesService = mRestAdapter.create(BeaconTypesService.class);
        beaconTypesService.getTypesForBeacon(beaconId, callback);
    }

    public static class Builder {
        private String mAccessToken;
        private Context mContext;

        public Builder setAccessToken(String apiKey) {
            mAccessToken = apiKey;
            return this;
        }

        public Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        private String getApiKey() {
            return this.mAccessToken;
        }

        public Context getContext() {
            return this.mContext;
        }

        public BeaconearAPI build() {
            if(mContext == null ) {
                throw new IllegalStateException("context not set");
            }
            if(mAccessToken == null ) {
                throw new IllegalStateException("access token not set");
            }
            return new BeaconearAPI(this);
        }
    }
}
