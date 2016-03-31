package com.wamel.beaconear.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wamel.beaconear.R;
import com.wamel.beaconear.callbacks.SelectionCallback;
import com.wamel.beaconear.core.FlowManager;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.User;
import com.wamel.beaconear.utils.MessagesUtil;

public class AppManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FlowManager mFlowManager;

    private RegisteredApplication mApplication;
    private User mUser;

    private Toolbar mToolbar;
    private TextView mAppName;
    private TextView mAppDescription;
    private TextView mAppPublicKey;
    private TextView mAppPrivateKey;
    private ImageView mAppIconImageView;
    private ImageView mDrawerAppIconImageView;
    private TextView mDrawerAppNameTextView;
    private TextView mDrawerUserEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initializeToolbar();
        getActivityParameters();

        if(validActivityParameters()) {
            initializeControls();
            fillAppData();
            mFlowManager = new FlowManager.Builder()
                    .setAccessToken("sarasa")
                    .setCurrentActivity(this)
                    .build();
        }
        else {
            finish();
        }
    }

    private void fillAppData() {
        mAppName.setText(mApplication.getName());
        if(mApplication.isActive()) {
            mAppName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_active), null, null, null);
        } else {
            mAppName.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_inactive), null, null, null);
        }
        mAppDescription.setText(mApplication.getDescription());

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(getString(R.string.public_key_label));
        textBuilder.append(" ");
        textBuilder.append(mApplication.getPublicKey());
        mAppPublicKey.setText(textBuilder.toString());

        textBuilder = new StringBuilder();
        textBuilder.append(getString(R.string.private_key_label));
        textBuilder.append(" ");
        textBuilder.append(mApplication.getPublicKey());
        mAppPrivateKey.setText(textBuilder.toString());

        if(mApplication.hasIconUrl()) {
            Picasso.with(this).load(mApplication.getIconUrl()).into(mAppIconImageView);
            if(mAppIconImageView.hasOnClickListeners()) {
                mAppIconImageView.setOnClickListener(null);
            }
        }
        else {
            mAppIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageInputDialog();
                }
            });
        }
        setDrawerInfo();
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
        mApplication.setIconUrl(iconUrl);
        fillAppData();
    }

    private boolean validActivityParameters() {
        return mApplication != null;
    }

    private void getActivityParameters() {
        mApplication = (RegisteredApplication) getIntent().getSerializableExtra("application");
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initializeControls() {
        initializeDrawerControls();
        mAppName = (TextView) findViewById(R.id.appName);
        mAppDescription = (TextView) findViewById(R.id.appDescription);
        mAppPublicKey = (TextView) findViewById(R.id.publicKey);
        mAppPrivateKey = (TextView) findViewById(R.id.privateKey);
        mAppIconImageView = (ImageView) findViewById(R.id.appIconImage);
    }

    private void initializeDrawerControls() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_app_manager);
        mDrawerAppIconImageView = (ImageView) headerView.findViewById(R.id.drawerIconImage);
        mDrawerAppNameTextView = (TextView) headerView.findViewById(R.id.applicationNametextView);
        mDrawerUserEmailTextView = (TextView) headerView.findViewById(R.id.userEmailTextView);
    }

    private void setDrawerInfo() {
        if(mApplication.hasIconUrl()) {
            Picasso.with(this).load(mApplication.getIconUrl()).into(mDrawerAppIconImageView);
        }
        mDrawerAppNameTextView.setText(mApplication.getName());
        mDrawerUserEmailTextView.setText(mUser.getEmail());
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beacons) {
            //startBeaconsSearchActivity();
        }
        else if (id == R.id.nav_types) {
            mFlowManager.startAppTypesActivity(mApplication, mUser);
        }
        else if (id == R.id.nav_edit) {
            startAppEditionActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startAppEditionActivity() {
        mFlowManager.startAppEditionActivity(mApplication, mUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FlowManager.EDITED_APP_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mApplication = (RegisteredApplication) data.getSerializableExtra("application");
                showSnackBar(mApplication.getName());
                fillAppData();
            }
        }
    }

    private void showSnackBar(String text) {
        String congratsMessage = getString(R.string.edited_app_congrats_message);
        congratsMessage = congratsMessage.replace("-appName-", text);
        Snackbar.make(mAppDescription, congratsMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
