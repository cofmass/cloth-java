package com.cofmass.clothRoomBackend.entity;

import lombok.Data;

@Data
public class Columns {
    private String title;
    private String dataIndex;

    private String key;
    private ScopedSlots scopedSlots;

    public Columns(String title, String dataIndex,String key) {
        this.title = title;
        this.dataIndex = dataIndex;
        this.key = key;
    }

    public Columns() {
    }
}
