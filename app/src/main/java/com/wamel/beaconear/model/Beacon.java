package com.wamel.beaconear.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mreverter on 26/3/16.
 */
public class Beacon implements Serializable {

    private String id;
    private String uuid;
    private String major;
    private String minor;
    private List<Type> types;

    public Beacon(String uuid, String major, String minor, String appPublicKey) {
        this.id = createId(uuid, major, minor, appPublicKey);
    }

    private String createId(String uuid, String major, String minor, String appPublicKey) {
        if(uuid != null && !uuid.isEmpty()
                && major != null && !major.isEmpty()) {
            StringBuilder idBuilder = new StringBuilder();
            idBuilder.append(appPublicKey);
            idBuilder.append("-");
            idBuilder.append(uuid);
            idBuilder.append("-");
            idBuilder.append(uuid);
            idBuilder.append("-");
            idBuilder.append(major);
            if (minor != null) {
                idBuilder.append("-");
                idBuilder.append(minor);
            }
            return idBuilder.toString();
        }
        else {
            throw new IllegalStateException("invalid beacon initialization");
        }
    }

    public String getBeaconId() {
        return this.id;
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public List<Type> getTypes() {
        return types;
    }
}
