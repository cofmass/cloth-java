package com.cofmass.clothRoomBackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.*;
import com.cofmass.clothRoomBackend.mapper.ClothesMapper;
import com.cofmass.clothRoomBackend.service.ClothesService;
import com.cofmass.clothRoomBackend.service.CommentsService;
import com.cofmass.clothRoomBackend.service.ImageService;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.file2Url;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@RestController
@RequestMapping("/clothes")
@Api(tags = "衣物管理")
public class ClothesController {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ClothesService clothesService;
    @Resource
    private ClothesMapper clothesMapper;

    @ApiOperation("显示衣橱")
    @GetMapping("/list")
    public R list() {
        User loginUser = userService.getLoginUser();
        List<Clothes> clothes = clothesService.list(new LambdaQueryWrapper<Clothes>().eq(Clothes::getUserId, loginUser.getUserId()));
        List<ClothesVo> clothesVos = new ArrayList<>();
        HashMap<String,List<ImageVo>> map = new HashMap<>();
        map.put("短袖",new ArrayList<ImageVo>());
        map.put("长袖",new ArrayList<ImageVo>());
        map.put("短裤",new ArrayList<ImageVo>());
        map.put("长裤",new ArrayList<ImageVo>());
        map.put("短裙",new ArrayList<ImageVo>());
        map.put("长裙",new ArrayList<ImageVo>());
        if(!clothes.isEmpty()){
            //        一个用户顶多六个
            for (Clothes c :clothes) {
    //            当前这个衣服的类型
                String clothesType = c.getClothesType();
                String clothesId = c.getClothesId();
    //            根据这个ID去image找相对应的图片们
                List<ImageVo> strings = map.get(clothesType);
                List<Image> images = imageService.list(new LambdaQueryWrapper<Image>().eq(Image::getImageId, clothesId));
                for (Image s :images) {
    //                转成base64给前端显示
                    strings.add(new ImageVo(s.getImageUrl(),convertFileToBase64(s.getImageUrl()),s.getImageUrl()));
                }
            }
        }
        Set<String> keys = map.keySet();
        for (String s :keys) {
            clothesVos.add(new ClothesVo(map.get(s),s));
        }
        return R.ok("成功").put("data",clothesVos);
    }

    @ApiOperation("添加衣物")
    @PostMapping("/add/{clothesType}/{clothId}")
    public R add(MultipartFile file, @PathVariable String clothesType,@PathVariable String clothId) throws IOException {
        if (file.isEmpty()){
            return R.error("传入了空的文件");
        }
        User loginUser = userService.getLoginUser();

        String newName = file2Url(file);
        file.transferTo(Paths.get(newName));

//        如果这个不存在那么说明没有给这个用户的这个类型的衣服添加过图片，那么就给他添加
        Clothes one = clothesService.getOne(new LambdaQueryWrapper<Clothes>().eq(Clothes::getClothesType, clothesType).eq(Clothes::getUserId, loginUser.getUserId()));
        if (one == null) {
            Clothes clothes = new Clothes();
            clothes.setClothesId(clothId);
            clothes.setClothesType(clothesType);
            clothes.setUserId(loginUser.getUserId());
            clothesService.save(clothes);
        }else { //剩下的这种情况就是添加过
            clothId = one.getClothesId();
//            对添加过的获得其clotheId然后再添加图片
        }
        imageService.save(new Image(clothId,newName));

        return R.ok("显示成功").put("imgUrl",convertFileToBase64(newName)).put("imageId",newName);
    }

    @ApiOperation("删除衣物")
    @DeleteMapping("/del")
    public R delById(@RequestParam String imageId) throws IOException {
         List<Image> images = imageService.list(new LambdaQueryWrapper<Image>().eq(Image::getImageUrl, imageId));
//         准备删除关联表
         if (images.size() == 1){
             Image image = images.get(0);
             clothesService.removeById(image.getImageId());
         }
        for (Image i :images) {
            String imageUrl = i.getImageUrl();
            Path path = Paths.get(imageUrl);
//            如果有这个图片就删除
            if (Files.exists(path)){
                Files.delete(path);
            }
        }

        imageService.remove(new LambdaQueryWrapper<Image>().eq(Image::getImageUrl, imageId));
        return R.ok("删除成功");
    }

    @ApiOperation("更换图片")
    @PostMapping("/change")
    public R add(MultipartFile file) throws IOException {
        if (file.isEmpty()){
            return R.error("传入了空的文件");
        }
        User loginUser = userService.getLoginUser();
        String newName = file2Url(file);
        file.transferTo(Paths.get(newName));
        Image image = new Image();
        String imageId = "CG"+loginUser.getUserId();
        image.setImageId(imageId);
        image.setImageUrl(newName);
        Image one = imageService.getOne(new LambdaQueryWrapper<Image>()
                .eq(Image::getImageId, imageId));
        if (one == null) {
            imageService.save(image);
        }else {
            imageService.update(image,new LambdaQueryWrapper<Image>()
                    .eq(Image::getImageId, imageId));
        }
        return R.ok("显示成功").put("imgUrl",convertFileToBase64(newName));
    }

    @ApiOperation("显示穿搭背景图")
    @GetMapping("/bgImg")
    public R bgImg(){
        User loginUser = userService.getLoginUser();
        String imgId = "CG"+loginUser.getUserId();
        Image one = imageService.getOne(new LambdaQueryWrapper<Image>().eq(Image::getImageId, imgId));
        if (one != null){
            return R.ok("显示成功").put("imgUrl",convertFileToBase64(one.getImageUrl())).put("bgUrl",one.getImageUrl());
        }else {
            return R.ok("没有图片");
        }
    }

    @ApiOperation("每日推荐衣服")
    @GetMapping("/recommended")
    public R recommended(){
//        如何推荐衣服
        String s = convertFileToBase64("E:\\VSCodeStore\\cloth-room\\src\\static\\images\\humen\\m2_out.jpeg");
        TestVo testVo = new TestVo();
        testVo.setText("测试");
        testVo.setIconPath(s);
        testVo.setImgId("这是图片ID");
        List<TestVo> testVos = new ArrayList<>();
        testVos.add(testVo);
        return R.ok("ok").put("data",testVos);
    }

    @GetMapping("/getNewClothNum")
    @ApiOperation("获取今日新增衣服数量")
    public R getNewClothNum(){
        int newClothNum = clothesMapper.getNewClothNum();
        return R.ok("查询成功").put("data",newClothNum);
    }

    @GetMapping("/getChange")
    @ApiOperation("获取更换衣服图片")
    public R getChange(@RequestParam String imageId){
        String localUrl = "";
        if (imageId.equals("E:\\冯廷楚杂物间\\毕设\\upload\\2025-03\\IMG20250327162611772.jpeg")){
            localUrl = "C:/Users/96307/Desktop/change_ok.png";
        }
        if (imageId.equals("E:\\冯廷楚杂物间\\毕设\\upload\\2025-03\\IMG20250328154903661.jpeg")){
            localUrl = "C:/Users/96307/Desktop/change_ok2.png";
        }
        String base64 = convertFileToBase64(localUrl);
        return R.ok("查询成功").put("data",base64);
    }

    @ApiOperation("获取用户列表分页")
    @PostMapping("/getClothPage")
    public R getClothPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "8") int pageSize, @RequestBody(required = false) ClothesSearchVo param) {
        Page<Clothes> clothesPage = clothesService.pageClothes(currentPage, pageSize,param);
        Page<ClothesUserVo> clothesVoPage = new Page();
//        复制了page这里的records没办法深拷贝
        BeanUtils.copyProperties(clothesPage,clothesVoPage);
//        对records进行深拷贝
        List<ClothesUserVo> records = new ArrayList<>();
        for (Clothes c : clothesPage.getRecords()) {
            ClothesUserVo clothesUserVo = new ClothesUserVo();
            BeanUtils.copyProperties(c,clothesUserVo);
            records.add(clothesUserVo);
        }
        for (ClothesUserVo clothes :records) {
            Image image = imageService.getOne(new LambdaQueryWrapper<Image>()
                    .eq(Image::getImageId, clothes.getClothesId())
            );
            if (image!=null){
                clothes.setImgUrl(convertFileToBase64(image.getImageUrl()));
            }
            User user = userService.getById(clothes.getUserId());
            if (user != null){
                clothes.setUserName(user.getUserName());
            }else {
                clothes.setUserName("未知用户");
            }
        }
        clothesVoPage.setRecords(records);
        return R.ok("查询成功").put("data", clothesVoPage);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除衣服")
    public R deleteById(@RequestParam String clothesId){
        clothesService.removeById(clothesId);
        return R.ok("删除成功");
    }
}
