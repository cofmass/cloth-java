package com.cofmass.clothRoomBackend.entity;

import lombok.Data;

@Data
public class ScopedSlots {
    private String customRender;

    public ScopedSlots(String customRender) {
        this.customRender = customRender;
    }
}
