package com.example.deviceapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceState {

    AVAILABLE("available"),
    IN_USE("in-use"),
    INACTIVE("inactive");

    private final String value;

    public static DeviceState fromString(String value) {
        for (DeviceState state : DeviceState.values()) {
            if (state.value.equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid device state: " + value);
    }
}
