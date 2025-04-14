package com.cofmass.clothRoomBackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.Image;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.mapper.UserMapper;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.UserSearchVo;
import com.cofmass.clothRoomBackend.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.baseUrl;
import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.file2Url;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserMapper userMapper;

    @ApiOperation("获取用户信息(id)")
    @GetMapping("/id")
    public R getById(@RequestParam String userId) {
        User user = userService.getById(userId);
        if (user != null) {
            user.setBgUrl(convertFileToBase64(user.getBgUrl()));
            user.setAvatarUrl(convertFileToBase64(user.getAvatarUrl()));
            return R.ok("查询成功").put("data", user);
        }
        return R.error("查询失败");
    }

    @ApiOperation("添加用户信息")
    @PostMapping("/reg")
    public R add(@RequestBody User user) throws IOException {
        if (userService.getById(user) != null) {
            return R.error("用户已注册");
        }
        String avatarUrl = user.getAvatarUrl();
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
        String imgName = "AV"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String savePath = baseUrl+date+imgName;
        try {
            URL url = new URL(avatarUrl);
            try (InputStream in = url.openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(savePath)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.println("图片下载完成，保存路径：" + savePath);
                user.setAvatarUrl(savePath);
            } catch (Exception e) {
                System.err.println("下载图片时发生错误：" + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("无法解析图片链接：" + e.getMessage());
        }
        boolean save = userService.save(user);
        if (save) {
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody User loginUser, HttpServletRequest request) {
//        第一次登录可能没有token
        String phone = loginUser.getPhone();
        String password = loginUser.getPassword();
        String loginToken = request.getHeader("token");
        if (loginToken == null || loginToken.isEmpty()) {
            User user1 = userService.login(phone, password);
            user1.setBgUrl(convertFileToBase64(user1.getBgUrl()));
            user1.setAvatarUrl(convertFileToBase64(user1.getAvatarUrl()));
            String token = UUID.randomUUID().toString();
            // 设置键值对并指定过期时间 30分钟
            redisTemplate.opsForValue().set(token, user1, 12, TimeUnit.HOURS);
            return R.ok().put("data", user1).put("token", token);
        }
        User u = (User) redisTemplate.opsForValue().get(loginToken);
//        过期的情况
        if (u == null) {
            User user2 = userService.login(phone, password);
            user2.setBgUrl(convertFileToBase64(user2.getBgUrl()));
            user2.setAvatarUrl(convertFileToBase64(user2.getAvatarUrl()));
            String token = UUID.randomUUID().toString();
            // 设置键值对并指定过期时间 30分钟
            redisTemplate.opsForValue().set(token, user2, 12, TimeUnit.HOURS);
            return R.ok().put("data", user2).put("token", token);
        }
        if (u != null) {
            return R.ok().put("data", u).put("token", loginToken);
        }

        return R.error("查询失败");
    }

    @ApiOperation("忘记密码")
    @PostMapping("/forget")
    public R forget(@RequestBody UserVo vo) {
//        传入手机号和密码
        User loginUser = userService.getLoginUser();
        User one = userService.getById(loginUser.getUserId());
        if (one == null) {
//            根本就没注册过
            return R.error("请注册账户");
        }
        one.setPassword(vo.getPassword());
        userService.updateById(one);
        return R.ok("修改成功");
    }

    @PostMapping("/changeAvatar")
    @ApiOperation("修改头像")
    public R changeAvatar(MultipartFile file) throws IOException {
        User loginUser = userService.getLoginUser();
        String newName = file2Url(file);
        if (file.isEmpty()) {
            return R.error("传入了空的文件");
        }
        loginUser.setAvatarUrl(newName);
        userService.updateById(loginUser);
        file.transferTo(Paths.get(newName));
        return R.ok("修改头像成功");
    }

    @PostMapping("/changeBackGround")
    @ApiOperation("修改头像")
    public R changeBackGround(MultipartFile file) throws IOException {
        User loginUser = userService.getLoginUser();
        String newName = file2Url(file);
        if (file.isEmpty()) {
            return R.error("传入了空的文件");
        }
        loginUser.setBgUrl(newName);
        userService.updateById(loginUser);
        file.transferTo(Paths.get(newName));
        return R.ok("修改背景成功");
    }

    @PostMapping("/getUserPage")
    @ApiOperation("用户分页")
    public R getUserPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "8") int pageSize, @RequestBody(required = false) UserSearchVo param) throws IOException {
        System.out.println("这里"+param);
        Page<User> userPage = userService.pageUser(currentPage, pageSize, param);
        List<User> records = userPage.getRecords();
        for (User user :records) {
            user.setAvatarUrl(convertFileToBase64(user.getAvatarUrl()));
            user.setBgUrl(convertFileToBase64(user.getBgUrl()));
        }
        userPage.setRecords(records);
        return R.ok("获取成功").put("data",userPage);
    }

    @GetMapping("/getNewUserNum")
    @ApiOperation("获取今日新增用户数量")
    public R getNewUserNum(){
        int newUserNum = userMapper.getNewUserNum();
        return R.ok("查询成功").put("data",newUserNum);
    }

    @PutMapping("/update")
    @ApiOperation("修改用户信息")
    public R updateUser(@RequestBody User user){
        userMapper.updateById(user);
        return R.ok("修改成功");
    }

    @DeleteMapping("/deleteById")
    @ApiOperation("删除用户")
    public R deleteUserById(@RequestParam String userId){
        userMapper.deleteById(userId);
        return R.ok("删除成功");
    }



}

