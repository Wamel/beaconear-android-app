package com.wamel.beaconear.core;

import android.app.Activity;
import android.content.Intent;

import com.wamel.beaconear.activities.AppManagerActivity;
import com.wamel.beaconear.activities.AppTypesActivity;
import com.wamel.beaconear.activities.BeaconSearchActivity;
import com.wamel.beaconear.activities.ApplicationFormActivity;
import com.wamel.beaconear.activities.TypeFormActivity;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.Type;
import com.wamel.beaconear.model.User;

/**
 * Created by mreverter on 26/3/16.
 */
public class FlowManager {

    public static final int TYPE_ACTIVITY_REQUEST_CODE = 1;
    public static final int APP_ACTIVITY_REQUEST_CODE = 2;

    private final Activity mCurrentActivity;
    private String mAccessToken;

    private FlowManager(Builder builder) {
        this.mCurrentActivity = builder.mCurrentActivity;
        this.mAccessToken = builder.mAccessToken;
    }

    public void startTypeFormActivity(RegisteredApplication application, User user) {
        Intent newTypeIntent = new Intent(mCurrentActivity, TypeFormActivity.class);
        newTypeIntent.putExtra("accessToken", mAccessToken);
        mCurrentActivity.startActivityForResult(newTypeIntent, TYPE_ACTIVITY_REQUEST_CODE);
    }

    public void startTypeFormActivity(RegisteredApplication application, User user, Type type) {
        Intent editTypeIntent = new Intent(mCurrentActivity, TypeFormActivity.class);
        editTypeIntent.putExtra("accessToken", mAccessToken);
        editTypeIntent.putExtra("type", type);
        mCurrentActivity.startActivityForResult(editTypeIntent, TYPE_ACTIVITY_REQUEST_CODE);
    }

    public void startBeaconSearchActivity() {
        Intent newTypeIntent = new Intent(mCurrentActivity, BeaconSearchActivity.class);
        newTypeIntent.putExtra("accessToken", mAccessToken);
        mCurrentActivity.startActivity(newTypeIntent);
    }

    public void startNewApplicationActivity() {
        Intent newAppIntent = new Intent(mCurrentActivity, ApplicationFormActivity.class);
        newAppIntent.putExtra("accessToken", mAccessToken);
        mCurrentActivity.startActivityForResult(newAppIntent, APP_ACTIVITY_REQUEST_CODE);
    }

    public void startAppManagerActivity(RegisteredApplication application, User user) {
        Intent appManagerIntent = new Intent(mCurrentActivity, AppManagerActivity.class);
        appManagerIntent.putExtra("application", application);
        appManagerIntent.putExtra("user", user);
        mCurrentActivity.startActivity(appManagerIntent);
    }

    public void startAppTypesActivity(RegisteredApplication application, User user) {
        Intent appTypesIntent = new Intent(mCurrentActivity, AppTypesActivity.class);
        appTypesIntent.putExtra("application", application);
        appTypesIntent.putExtra("user", user);
        mCurrentActivity.startActivity(appTypesIntent);
    }

    public void startAppEditionActivity(RegisteredApplication application, User user) {
        Intent newAppIntent = new Intent(mCurrentActivity, ApplicationFormActivity.class);
        newAppIntent.putExtra("accessToken", mAccessToken);
        newAppIntent.putExtra("application", application);
        mCurrentActivity.startActivityForResult(newAppIntent, APP_ACTIVITY_REQUEST_CODE);
    }

    public static class Builder {

        private Activity mCurrentActivity;
        private String mAccessToken;

        public Builder setCurrentActivity(Activity currentActivity) {
            this.mCurrentActivity = currentActivity;
            return this;
        }

        public Builder setAccessToken(String accessToken) {
            this.mAccessToken = accessToken;
            return this;
        }

        public FlowManager build() {
            return new FlowManager(this);
        }
    }
}
