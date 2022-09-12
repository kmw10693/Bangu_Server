package com.ott.ott_server.domain.enums;

public enum OttNames {
    NETFLIX("NETFLIX"),
    TVING("TVING"),
    WATCHA("WATCHAPLAY"),
    WAVVE("WAVVE");

    private final String value;

    OttNames(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

}
