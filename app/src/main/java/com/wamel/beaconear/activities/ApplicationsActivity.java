package com.wamel.beaconear.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wamel.beaconear.R;
import com.wamel.beaconear.adapters.ApplicationsAdapter;
import com.wamel.beaconear.callbacks.SelectionCallback;
import com.wamel.beaconear.core.BeaconearAPI;
import com.wamel.beaconear.core.FlowManager;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApplicationsActivity extends AppCompatActivity {

    private User mUser;
    private BeaconearAPI mBeaconearAPI;
    private FlowManager mFlowManager;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RecyclerView mApplicationsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        initializeToolbar();
        getActivityParameters();

        if(validActivityParameters()) {
            initializeControls();

            mFlowManager = new FlowManager.Builder()
                    .setAccessToken("sarasa")
                    .setCurrentActivity(this)
                    .build();

            mBeaconearAPI = new BeaconearAPI.Builder()
                    .setAccessToken("sarasa")
                    .setContext(this)
                    .build();
            mBeaconearAPI.getApplications(new Callback<List<RegisteredApplication>>() {
                @Override
                public void success(List<RegisteredApplication> registeredApplications, Response response) {
                    populateApplicationsList(registeredApplications);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
        else {
            finish();
        }
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void populateApplicationsList(List<RegisteredApplication> registeredApplications) {
        ApplicationsAdapter adapter = new ApplicationsAdapter(registeredApplications, new SelectionCallback<RegisteredApplication>() {
            @Override
            public void onSelected(RegisteredApplication application) {
                startAppManagerActivity(application);
            }
        });
        mApplicationsRecyclerView.setAdapter(adapter);
    }

    private void startAppManagerActivity(RegisteredApplication application) {
        mFlowManager.startAppManagerActivity(application, mUser);
    }

    private boolean validActivityParameters() {
        return mUser != null;
    }

    private void getActivityParameters() {
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initializeControls() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNewApplicationActivity();
                }
            });
        }
        mActivity = this;
        mApplicationsRecyclerView = (RecyclerView) findViewById(R.id.applications);
        mApplicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mApplicationsRecyclerView.setHasFixedSize(true);
    }

    private void startNewApplicationActivity() {
        mFlowManager.startNewApplicationActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FlowManager.NEW_APP_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                RegisteredApplication application = (RegisteredApplication) data.getSerializableExtra("application");
                showNewAppMessage(application.getName());
            }
        }
    }

    private void showNewAppMessage(String appName) {
        String congratsMessage = getString(R.string.new_app_congrats_message);
        congratsMessage = congratsMessage.replace("-appName-", appName);
        showSnackBar(congratsMessage);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mApplicationsRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
