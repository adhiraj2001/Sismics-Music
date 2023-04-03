package com.sismics.music.core.constant;

public enum AccessType {

    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    private String name;

    AccessType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
