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

    public static final int NEW_APP_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_TYPE_ACTIVITY_REQUEST_CODE = 2;
    public static final int EDITED_APP_ACTIVITY_REQUEST_CODE = 3;
    public static final int EDITED_TYPE_ACTIVITY_REQUEST_CODE = 4;

    private final Activity mCurrentActivity;
    private String mAccessToken;

    private FlowManager(Builder builder) {
        this.mCurrentActivity = builder.mCurrentActivity;
        this.mAccessToken = builder.mAccessToken;
    }

    public void startNewApplicationActivity() {
        Intent newAppIntent = new Intent(mCurrentActivity, ApplicationFormActivity.class);
        newAppIntent.putExtra("accessToken", mAccessToken);
        mCurrentActivity.startActivityForResult(newAppIntent, NEW_APP_ACTIVITY_REQUEST_CODE);
    }

    public void startAppEditionActivity(RegisteredApplication application, User user) {
        Intent newAppIntent = new Intent(mCurrentActivity, ApplicationFormActivity.class);
        newAppIntent.putExtra("accessToken", mAccessToken);
        newAppIntent.putExtra("user", user);
        newAppIntent.putExtra("application", application);
        mCurrentActivity.startActivityForResult(newAppIntent, EDITED_APP_ACTIVITY_REQUEST_CODE);
    }

    public void startNewTypeFormActivity(RegisteredApplication application, User user) {
        Intent newTypeIntent = new Intent(mCurrentActivity, TypeFormActivity.class);
        newTypeIntent.putExtra("accessToken", mAccessToken);
        newTypeIntent.putExtra("user", user);
        newTypeIntent.putExtra("application", application);
        mCurrentActivity.startActivityForResult(newTypeIntent, NEW_TYPE_ACTIVITY_REQUEST_CODE);
    }

    public void startTypeEditionActivity(RegisteredApplication application, User user, Type type) {
        Intent editTypeIntent = new Intent(mCurrentActivity, TypeFormActivity.class);
        editTypeIntent.putExtra("accessToken", mAccessToken);
        editTypeIntent.putExtra("user", user);
        editTypeIntent.putExtra("application", application);
        editTypeIntent.putExtra("type", type);
        mCurrentActivity.startActivityForResult(editTypeIntent, EDITED_TYPE_ACTIVITY_REQUEST_CODE);
    }

    public void startBeaconSearchActivity() {
        Intent newTypeIntent = new Intent(mCurrentActivity, BeaconSearchActivity.class);
        newTypeIntent.putExtra("accessToken", mAccessToken);
        mCurrentActivity.startActivity(newTypeIntent);
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
