package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Work;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class WorkVo extends Work {
    private Integer likeNums;
    private Integer favorNums;
    private List<String> images;

}
