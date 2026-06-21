package com.dnspex.util.enumeration;

public enum UserState {
    INACTIVE, // not verified
    ACTIVE, // verified
    DELETED // set as deleted but not from database -> (30d)
}
