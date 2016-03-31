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
import android.widget.EditText;

import com.wamel.beaconear.R;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.Type;
import com.wamel.beaconear.model.User;

public class TypeFormActivity extends AppCompatActivity {

    private RegisteredApplication mApplication;
    private User mUser;
    private Type mType;

    private EditText mTypeNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_type);
        initializeToolbar();
        getActivityParameters();
        initializeControls();
        if(isEdition()) {
            setTitle(getString(R.string.title_activity_edit_type));
            fillData();
        }
    }

    private void fillData() {
            mTypeNameEditText.setText(mType.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.type_form, menu);
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
        mTypeNameEditText = (EditText) findViewById(R.id.typeNameEditText);
    }

    private boolean isEdition() {
        return mType != null;
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

    private void getActivityParameters() {
        mApplication = (RegisteredApplication) getIntent().getSerializableExtra("application");
        mUser = (User) getIntent().getSerializableExtra("user");
        mType = (Type) getIntent().getSerializableExtra("type");
    }

    private void submitForm() {
        if(validInputs()) {
            Type type = new Type(mTypeNameEditText.getText().toString());

            //TODO Post a types
            finishWithTypeResult(type);
        }
    }

    private void finishWithTypeResult(Type type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validInputs() {
        return mTypeNameEditText.getText() != null && !mTypeNameEditText.getText().toString().isEmpty();
    }

}
