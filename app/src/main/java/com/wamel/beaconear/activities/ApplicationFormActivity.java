package com.wamel.beaconear.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.wamel.beaconear.R;
import com.wamel.beaconear.model.RegisteredApplication;

public class ApplicationFormActivity extends AppCompatActivity {

    private EditText mAppNameEditText;
    private EditText mAppDescriptionEditText;
    private Switch mActiveSwitch;
    private RegisteredApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_app);
        getActivityParameters();
        initializeToolbar();
        initializeControls();
    }

    private void getActivityParameters() {
        mApplication = (RegisteredApplication) getIntent().getSerializableExtra("application");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_form, menu);
        Drawable drawable = menu.findItem(R.id.item_submit).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_submit) {
            submitForm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeControls() {

        mAppNameEditText = (EditText) findViewById(R.id.appNameEditText);
        mAppDescriptionEditText = (EditText) findViewById(R.id.appDescriptionEditText);
        mActiveSwitch = (Switch) findViewById(R.id.activeSwitch);
        mActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mActiveSwitch.setText(getString(R.string.active));
                } else {
                    mActiveSwitch.setText(R.string.inactive);
                }
            }
        });

        if(mApplication != null) {
            mAppNameEditText.setText(mApplication.getName());
            mAppDescriptionEditText.setText(mApplication.getDescription());
            mActiveSwitch.setChecked(mApplication.isActive());
        }
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void submitForm() {
        if(validInputs()) {
            RegisteredApplication application = new RegisteredApplication();
            application.setName(mAppNameEditText.getText().toString());
            application.setDescription(mAppDescriptionEditText.getText().toString());
            application.setActive(mActiveSwitch.isChecked());

            //TODO Post a applications
            finishWithAppResult(application);
        }
    }

    private void finishWithAppResult(RegisteredApplication application) {
        Intent appIntent = new Intent();
        appIntent.putExtra("application", application);
        setResult(RESULT_OK, appIntent);
        finish();
    }

    private boolean validInputs() {
        return mAppNameEditText.getText() != null && !mAppNameEditText.getText().toString().isEmpty()
                && mAppDescriptionEditText.getText() != null && !mAppDescriptionEditText.getText().toString().isEmpty();
    }

}
