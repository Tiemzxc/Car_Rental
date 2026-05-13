package com.carrental.model;

public class Customer extends User {
    private String drivingLicense;

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    @Override
    public String getDisplayName() {
        return getFirstName() + " " + getLastName();
    }
}