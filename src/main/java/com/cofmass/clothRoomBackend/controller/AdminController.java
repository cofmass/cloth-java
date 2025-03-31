package com.cofmass.clothRoomBackend.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Image;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.mapper.AdminMapper;
import com.cofmass.clothRoomBackend.service.AdminService;
import com.cofmass.clothRoomBackend.service.ImageService;
import com.cofmass.clothRoomBackend.utils.HttpRequestUtil;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.AdminSearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cofmass.clothRoomBackend.utils.HttpStatus.UPDATE_MY;
import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.file2Url;
import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.fileSave;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.imgToBase64;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员管理")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ImageService imageService;

    @ApiOperation("管理员注册")
    @PostMapping("/register")
    public R register(@RequestParam(required = false)MultipartFile file,@RequestParam(name = "admin") String adminJson) throws IOException {
        Admin admin = JSON.parseObject(adminJson, Admin.class);
        if (admin == null){
        return R.error("您的网络不好请重试");
        }
        String localPath = fileSave(file);
        if (admin.getAdminName().isEmpty() || admin.getPhone().isEmpty()) {
            return R.error("用户名或者手机号不能为空");
        }

        if (admin.getPassword().isEmpty()) {
            return R.error("密码不能为空");
        }

        admin.setAdminId("ADMIN"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        admin.setAvatarUrl(localPath);
        List<Admin> list = adminService.list(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getPhone, admin.getPhone())
                .or()
                .eq(Admin::getAdminName, admin.getAdminName())
        );
        if (list.isEmpty()){
            if (adminService.save(admin)) {
                return R.ok("添加成功");
            }
        }else {
            return R.error("该手机号或用户名已注册");
        }

        return R.error("添加失败");
    }


    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public R login(@RequestBody Admin admin, HttpServletRequest request) {
        String loginToken = request.getHeader("token");
//        token不存在(过期和本身就没有的情况)
        if (loginToken == null || loginToken.isEmpty()) {
//            登录成功的话
            Admin one = adminService.getOne(new LambdaQueryWrapper<Admin>()
                    .eq(Admin::getPassword, admin.getPassword())
                    .and(wrapper -> wrapper
                            .eq(Admin::getAdminName, admin.getAdminName())
                            .or()
                            .eq(Admin::getPhone, admin.getAdminName())
                    ));
            if (one != null) {
//                说明密码且用户名正确
                String token = UUID.randomUUID().toString();
                // 设置键值对并指定过期时间 30分钟
                redisTemplate.opsForValue().set(token, one, 12, TimeUnit.HOURS);
                one.setAvatarUrl(convertFileToBase64(one.getAvatarUrl()));
                return R.ok("查询成功").put("data", one).put("token", token);
            }
            return R.error("用户名或密码错误");
        }
//        token存在去redis找
        Admin loginAdmin = (Admin) redisTemplate.opsForValue().get(loginToken);
        if (loginAdmin != null) {
            return R.ok("查询成功").put("data", loginAdmin);
        }
        return R.error("查询失败");
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("/token")
    public R register(@RequestParam String token) {
        if (token != null && !token.equals("")) {
            Admin admin = (Admin) redisTemplate.opsForValue().get(token);
            admin.setAvatarUrl(convertFileToBase64(admin.getAvatarUrl()));
            if (admin != null) {
                return R.ok("查询成功").put("data", admin);
            }
        }
        return R.error("添加失败");
    }

    @ApiOperation("头像上传")
    @PostMapping("/avatarUpload")
    public R avatarUpload(MultipartFile file) throws IOException {
        if (file.isEmpty()){
            return R.error("传入了空的文件");
        }
        Admin loginUser = adminService.getLoginUser();
        if (loginUser == null) {
            return R.error("未登录");
        }
        String newName = file2Url(file);
        file.transferTo(Paths.get(newName));
        loginUser.setAvatarUrl(newName);
        adminService.updateById(loginUser);
        imageService.save(new Image(loginUser.getAdminId(),newName));
        return R.ok("头像上传成功").put("imgUrl",convertFileToBase64(newName));
    }

    @ApiOperation("获取用户列表分页")
    @PostMapping("/getAdminPage")
    public R getAdminPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "8") int pageSize, @RequestBody(required = false)AdminSearchVo param) {
        Page<Admin> adminPage = adminService.pageAdmin(currentPage, pageSize,param);
        List<Admin> records = adminPage.getRecords();
        for (Admin admin :records) {
            admin.setAvatarUrl(convertFileToBase64(admin.getAvatarUrl()));
        }
        adminPage.setRecords(records);
        return R.ok("查询成功").put("data", adminPage);
    }

    @ApiOperation("根据Id删除管理员")
    @GetMapping("/delAdminById")
    public R delAdminById(@RequestParam String adminId){
        adminService.removeById(adminId);
        return R.ok("删除成功");
    }

    @ApiOperation("获取管理员头像预览")
    @PostMapping("/getPreView")
    public R getPreView(@RequestParam(value = "file", required = false) MultipartFile file) {
        String base64 = imgToBase64(file);
        return R.ok("显示成功").put("data", base64);
    }

    @ApiOperation("修改管理员")
    @PutMapping("/update")
    public R updateById(@RequestBody Admin admin) {
        Admin loginUser = adminService.getLoginUser();
        adminService.updateById(admin);
        if (admin.getAdminId().equals(loginUser.getAdminId())){
//            删除用户的token
            redisTemplate.delete(HttpRequestUtil.getRequest().getHeader("token"));
            return R.error(UPDATE_MY,"信息已经更改请重新登录");
        }
        return R.ok("显示成功");
    }

}
