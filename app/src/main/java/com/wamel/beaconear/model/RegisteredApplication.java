package com.wamel.beaconear.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mreverter on 27/3/16.
 */
public class RegisteredApplication implements Serializable {

    private String id;
    private String name;
    private String description;
    private Boolean active;
    private String iconUrl;
    private String publicKey;
    private List<Type> types;
    private List<Beacon> beacons;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() {
        return active;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
