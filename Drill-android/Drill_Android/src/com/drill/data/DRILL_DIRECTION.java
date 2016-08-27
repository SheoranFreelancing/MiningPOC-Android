package com.drill.data;

/**
 * Created by Kamesh on 8/27/16.
 */

public enum DRILL_DIRECTION {

    GOING_DOWN(-1),
    NONE(0),
    GOING_UP(1);

    private final int value;

    private DRILL_DIRECTION(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
