package com.wamel.beaconear.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.wamel.beaconear.R;
import com.wamel.beaconear.adapters.TypesAdapter;
import com.wamel.beaconear.callbacks.TypeSelectedCallback;
import com.wamel.beaconear.core.FlowManager;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.Type;
import com.wamel.beaconear.model.User;

public class AppTypesActivity extends AppCompatActivity {

    private RegisteredApplication mApplication;
    private User mUser;

    private FlowManager mFlowManager;

    private RecyclerView mTypesRecyclerView;
    private EditText mTypeNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_types);
        initializeToolbar();
        initializeControls();
        getActivityParameters();
        mFlowManager = new FlowManager.Builder()
                .setAccessToken("sarasa")
                .setCurrentActivity(this)
                .build();
        populateTypesRecyclerView();
    }

    private void populateTypesRecyclerView() {
        TypesAdapter adapter = new TypesAdapter(mApplication.getTypes(), new TypeSelectedCallback() {
            @Override
            public void onSelected(Type type) {
                startTypeFormForEditing(type);
            }
        });
        mTypesRecyclerView.setAdapter(adapter);
    }

    private void getActivityParameters() {
        mApplication = (RegisteredApplication) getIntent().getSerializableExtra("application");
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initializeControls() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startTypeForm();
                }
            });
        }
        mTypesRecyclerView = (RecyclerView) findViewById(R.id.types);
        mTypesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTypesRecyclerView.setHasFixedSize(true);
    }

    private void startTypeForm() {
        mFlowManager.startTypeFormActivity(mApplication, mUser);
    }

    private void startTypeFormForEditing(Type type) {
        mFlowManager.startTypeFormActivity(mApplication, mUser, type);
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FlowManager.TYPE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("type");
                showSnackBar(type.getName(), mApplication.getName());
            }
        }
    }

    private void showSnackBar(String typeName, String appName) {
        String congratsMessage = getString(R.string.new_type_congrats_message);
        congratsMessage = congratsMessage.replace("-typeName-", typeName);
        congratsMessage = congratsMessage.replace("-appName-", appName);
        Snackbar.make(mTypesRecyclerView, congratsMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
