package com.wamel.beaconear.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wamel.beaconear.R;
import com.wamel.beaconear.core.BeaconearAPI;
import com.wamel.beaconear.core.FlowManager;
import com.wamel.beaconear.model.RegisteredApplication;
import com.wamel.beaconear.model.User;


public class AppManagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BeaconearAPI mBeaconearAPI;
    private FlowManager mFlowManager;

    private RegisteredApplication mApplication;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RecyclerView mApplicationsRecyclerView;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initializeToolbar();

        getActivityParameters();

        if(validActivityParameters()) {
            initializeControls();
            mBeaconearAPI = new BeaconearAPI.Builder()
                    .setAccessToken("sarasa")
                    .setContext(this)
                    .build();
            mFlowManager = new FlowManager.Builder()
                    .setAccessToken("sarasa")
                    .setCurrentActivity(this)
                    .build();
        }
        else {
            finish();
        }
    }

    private boolean validActivityParameters() {
        return mApplication != null;
    }

    private void getActivityParameters() {
        mApplication = (RegisteredApplication) getIntent().getSerializableExtra("application");
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    private void initializeControls() {
        initializeDrawer();

        mActivity = this;
        mApplicationsRecyclerView = (RecyclerView) findViewById(R.id.applications);
        mApplicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mApplicationsRecyclerView.setHasFixedSize(true);
    }

    private void initializeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_app_manager);

        TextView headerText = (TextView) headerView.findViewById(R.id.applicationNametextView);
        TextView userEmailText = (TextView) headerView.findViewById(R.id.userEmailTextView);

        headerText.setText(mApplication.getName());
        userEmailText.setText(mUser.getEmail());

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
        if(requestCode == FlowManager.APP_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                RegisteredApplication application = (RegisteredApplication) data.getSerializableExtra("application");
                showSnackBar(application.getName());
            }
        }
    }
    private void showSnackBar(String text) {
        String congratsMessage = getString(R.string.edited_app_congrats_message);
        congratsMessage = congratsMessage.replace("-appName-", text);
        Snackbar.make(mApplicationsRecyclerView, congratsMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
