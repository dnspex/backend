package com.dnspex.util.enumeration;

public enum MonitorStatus {
    OPERATIONAL,
    MISMATCH, //current domain value dont match with db record value from monitor
    UNREACHABLE, //domain not exist/registered <<>> nameserver from provider dont get domain
    NOT_FOUND, //dns record not exist
    PENDING // domain was not checked since creation
}