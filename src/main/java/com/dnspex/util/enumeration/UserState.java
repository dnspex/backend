package com.dnspex.util.enumeration;

public enum UserState {
    INACTIVE, // not verified
    ACTIVE, // verified
    DEACTIVATED // set as DEACTIVATED but not from database -> (30d)
}
