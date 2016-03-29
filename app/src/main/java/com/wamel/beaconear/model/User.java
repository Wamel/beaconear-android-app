package com.wamel.beaconear.model;

import java.io.Serializable;

/**
 * Created by mreverter on 28/3/16.
 */
public class User implements Serializable {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
