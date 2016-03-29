package com.wamel.beaconear.core;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;

/**
 * Created by mreverter on 26/3/16.
 */
public class WMBeaconManager implements BeaconConsumer {

    final String EDDYSTONE_LAYOUT = "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19";
    final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager mBeaconManager;
    private Context context;

    public WMBeaconManager(MonitorNotifier monitorNotifier, RangeNotifier rangeNotifier, Context context)
    {
        this.context = context;
        mBeaconManager = BeaconManager.getInstanceForApplication(this.context);
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(IBEACON_LAYOUT));
        mBeaconManager.setRangeNotifier(rangeNotifier);
        mBeaconManager.setMonitorNotifier(monitorNotifier);
    }

    public void startScan() {
        mBeaconManager.bind(this);
    }

    public void stopScanning() {
        if(mBeaconManager != null && mBeaconManager.isBound(this))
            mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            mBeaconManager.startRangingBeaconsInRegion(new org.altbeacon.beacon.Region("all", null, null, null));
        } catch (RemoteException e) {
            Log.println(Log.ERROR, "onBeaconServiceConnect", e.getMessage());
        }
    }

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.context.bindService(intent, serviceConnection, i);
    }
}
