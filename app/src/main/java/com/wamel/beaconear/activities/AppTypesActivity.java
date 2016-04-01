package com.wamel.beaconear.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.wamel.beaconear.R;
import com.wamel.beaconear.adapters.TypesAdapter;
import com.wamel.beaconear.callbacks.LongSelectionCallback;
import com.wamel.beaconear.callbacks.SelectionCallback;
import com.wamel.beaconear.core.FlowManager;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.Type;
import com.wamel.beaconear.model.User;
import com.wamel.beaconear.utils.AnimationsUtil;

public class AppTypesActivity extends AppCompatActivity {

    private RegisteredApplication mApplication;
    private User mUser;
    private Type mSelectedType;

    private FlowManager mFlowManager;

    private RecyclerView mTypesRecyclerView;
    private View mSelectedTypeView;
    private Drawable mRegularRowBackGround;

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

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        View editView = getLayoutInflater().inflate(R.layout.toolbar_edition_layout, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(editView, layout);
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

    private void populateTypesRecyclerView() {

        SelectionCallback<Type> selectionCallback = new SelectionCallback<Type>() {
            @Override
            public void onSelected(Type type) {
                if(isRowSelected()) {
                    undoRowSelection();
                }
            }
        };

        LongSelectionCallback<Type> longSelectionCallback = new LongSelectionCallback<Type>() {
            @Override
            public void onSelected(Type selected, View view) {
                if(!isRowSelected()) {
                    showEditionMenu();
                }
                setViewSelection(selected, view);
            }
        };

        TypesAdapter adapter = new TypesAdapter(mApplication.getTypes(), selectionCallback, longSelectionCallback);
        mTypesRecyclerView.setAdapter(adapter);
    }

    private void setViewSelection(Type selected, View view) {
        if(selectedRowChanged(selected)) {
            if(isRowSelected()) {
                setPreviousSelectedRowOriginalState();
            }
            mSelectedType = selected;
            mSelectedTypeView = view;
            markRowAsSelected();
        } else {
            undoRowSelection();
        }
    }

    private boolean selectedRowChanged(Type selected) {
        return (mSelectedType == null) || !mSelectedType.getName().equals(selected.getName());
    }

    private void markRowAsSelected() {

        mRegularRowBackGround = mSelectedTypeView.getBackground();
        mSelectedTypeView.setBackgroundColor(ContextCompat.getColor(this, R.color.light_blue));
    }

    private void setPreviousSelectedRowOriginalState() {
        mSelectedTypeView.setBackground(mRegularRowBackGround);
    }

    private boolean isRowSelected() {
        return mSelectedTypeView != null;
    }

    private void showEditionMenu() {
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        AnimationsUtil.expand(getSupportActionBar().getCustomView());
    }

    private void startTypeForm() {
        mFlowManager.startNewTypeFormActivity(mApplication, mUser);
    }

    private void startTypeFormForEditing(Type type) {
        mFlowManager.startTypeEditionActivity(mApplication, mUser, type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FlowManager.NEW_TYPE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("type");
                showNewTypeMessage(type.getName(), mApplication.getName());
            }
        }
        if(requestCode == FlowManager.EDITED_TYPE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Type type = (Type) data.getSerializableExtra("type");
                showEditedTypeMessage(type.getName());
            }
        }
    }

    private void showNewTypeMessage(String typeName, String appName) {
        String congratsMessage = getString(R.string.new_type_congrats_message);
        congratsMessage = congratsMessage.replace("-typeName-", typeName);
        congratsMessage = congratsMessage.replace("-appName-", appName);
        showSnackBar(congratsMessage);
    }

    private void showEditedTypeMessage(String typeName) {
        String congratsMessage = getString(R.string.edited_type_congrats_message);
        congratsMessage = congratsMessage.replace("-typeName-", typeName);
        showSnackBar(congratsMessage);
    }

    private void showSnackBar(String message) {
        Snackbar.make(mTypesRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        if(isRowSelected()) {
            undoRowSelection();
        } else {
            super.onBackPressed();
        }
    }

    private void undoRowSelection() {
        AnimationsUtil.collapse(getSupportActionBar().getCustomView());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSelectedTypeView.setBackground(mRegularRowBackGround);
        mSelectedType = null;
        mSelectedTypeView = null;
    }

    public void editClicked(View view) {
        startTypeFormForEditing(mSelectedType);
    }

    public void deleteClicked(View view) {

    }
}
