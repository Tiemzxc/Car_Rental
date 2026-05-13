package com.carrental.model;

public class Admin extends User {
    @Override
    public String getDisplayName() {
        return "Admin " + getFirstName();
    }
}