package com.wamel.beaconear.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.wamel.beaconear.R;
import com.wamel.beaconear.core.BeaconearAPI;
import com.wamel.beaconear.core.WMBeaconManager;
import com.wamel.beaconear.model.Type;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;

import java.util.Collection;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BeaconSearchActivity extends AppCompatActivity {

    private WMBeaconManager mBeaconManager;
    private RangeNotifier mRangeNotifier;
    private BeaconearAPI mBeaconearApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBeaconearApi = new BeaconearAPI.Builder()
                .setAccessToken("sarasa")
                .setContext(this)
                .build();

        mRangeNotifier = new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {
                startTypesActivity();
            }
        };
        mBeaconManager = new WMBeaconManager(null, mRangeNotifier, this);
    }

    private void startTypesActivity() {
        mBeaconManager.stopScanning();
        mBeaconearApi.getTypesForBeacon("1-1-1-1", new Callback<List<Type>>() {
            @Override
            public void success(final List<Type> types, Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BeaconSearchActivity.this, "" + types.size(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(final RetrofitError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RetrofitError error1 = error;
                        Toast.makeText(BeaconSearchActivity.this, "FUCK", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mBeaconManager.startScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mBeaconManager.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mBeaconManager.stopScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.stopScanning();
    }

}
