package com.cofmass.clothRoomBackend.vo;

import com.cofmass.clothRoomBackend.entity.Admin;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminUploadVo {
    private MultipartFile file;
    private Admin admin;
}
