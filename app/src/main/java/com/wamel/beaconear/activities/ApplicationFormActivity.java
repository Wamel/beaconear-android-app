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
import android.widget.ImageView;
import android.widget.Switch;

import com.squareup.picasso.Picasso;
import com.wamel.beaconear.R;
import com.wamel.beaconear.callbacks.SelectionCallback;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.utils.MessagesUtil;

public class ApplicationFormActivity extends AppCompatActivity {

    private EditText mAppNameEditText;
    private EditText mAppDescriptionEditText;
    private Switch mActiveSwitch;
    private ImageView mAppIconImageView;

    private RegisteredApplication mApplication;
    private String mApplicationUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_app);
        getActivityParameters();
        initializeToolbar();
        initializeControls();
        if(isEdition()) {
            setTitle(getString(R.string.title_activity_edit_app));
            fillData();
        }
    }

    private void fillData() {
        mAppNameEditText.setText(mApplication.getName());
        mAppDescriptionEditText.setText(mApplication.getDescription());
        mActiveSwitch.setChecked(mApplication.isActive());
        mApplicationUrl = mApplication.getIconUrl();
        if(mApplication.hasIconUrl()) {
            Picasso.with(this).load(mApplicationUrl).into(mAppIconImageView);
        }
    }

    private boolean isEdition() {
        return mApplication != null;
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

    private void initializeControls() {

        mAppNameEditText = (EditText) findViewById(R.id.appNameEditText);
        mAppDescriptionEditText = (EditText) findViewById(R.id.appDescriptionEditText);
        mActiveSwitch = (Switch) findViewById(R.id.activeSwitch);
        mActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mActiveSwitch.setText(getString(R.string.active));
                } else {
                    mActiveSwitch.setText(R.string.inactive);
                }
            }
        });
        mAppIconImageView = (ImageView) findViewById(R.id.appIconImage);
        mAppIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageInputDialog();
            }
        });
    }

    private void showImageInputDialog() {
        MessagesUtil.showInputDialog(this, getString(R.string.app_icon_url_input_title), new SelectionCallback<String>() {
            @Override
            public void onSelected(String selected) {
                setApplicationIconUrl(selected);
            }
        });
    }

    private void setApplicationIconUrl(String iconUrl) {
        //TODO post a applications
        mApplicationUrl = iconUrl;
        if(mApplicationUrl != null && !mApplicationUrl.isEmpty()) {
            Picasso.with(this).load(mApplicationUrl).into(mAppIconImageView);
        }
    }

    private void submitForm() {
        if(validInputs()) {
            RegisteredApplication applicationResult;
            if(isEdition()) {
                mApplication.setName(mAppNameEditText.getText().toString());
                mApplication.setDescription(mAppDescriptionEditText.getText().toString());
                mApplication.setActive(mActiveSwitch.isChecked());
                mApplication.setIconUrl(mApplicationUrl);
                applicationResult = mApplication;
            } else {
                applicationResult = new RegisteredApplication();
                applicationResult.setName(mAppNameEditText.getText().toString());
                applicationResult.setDescription(mAppDescriptionEditText.getText().toString());
                applicationResult.setActive(mActiveSwitch.isChecked());
                applicationResult.setIconUrl(mApplicationUrl);
            }
            //TODO Post a applications
            finishWithAppResult(applicationResult);
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
