package com.cofmass.clothRoomBackend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ImageLocalUrl {
//    上传到当地文件路径
    public final static String baseUrl = "E:/冯廷楚杂物间/毕设/upload/";

//    其实就是改名
    public static String file2Url(MultipartFile file) throws IOException {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 创建日期格式化器，格式为 "yyyymm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化当前日期 当前日期文件夹
        String date = currentDate.format(formatter) + "/";
        Path path = Paths.get(baseUrl + date);
//        判断文件是否存在
        if (!Files.exists(path)) {
            // 文件不存在，则创建文件
            Files.createDirectories(path);
        }

        String originalFilename = file.getOriginalFilename();
        originalFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
        String imgName = "IMG"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+originalFilename;
        return baseUrl+date+imgName;
    }


//    将图片保存到本地并且返回路径
    public static String fileSave(MultipartFile file) throws IOException {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 创建日期格式化器，格式为 "yyyymm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化当前日期 当前日期文件夹
        String date = currentDate.format(formatter) + "/";
        Path path = Paths.get(baseUrl + date);

        // 判断文件是否存在
        if (!Files.exists(path)) {
            // 文件不存在，则创建文件
            Files.createDirectories(path);
        }

        String originalFilename = file.getOriginalFilename();
        originalFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
        String imgName = "IMG" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + originalFilename;
        Path targetPath = path.resolve(imgName);

        // 将文件保存到本地文件系统
        Files.write(targetPath, file.getBytes());

        return targetPath.toString();
    }
}
