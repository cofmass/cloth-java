package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Columns;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.entity.WorkData;
import lombok.Data;

import java.util.List;

@Data
public class WorkListVo{
    private List<Columns> columns;
    private List<WorkData> data;
}
