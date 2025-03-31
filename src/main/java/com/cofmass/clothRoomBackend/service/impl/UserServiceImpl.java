package com.cofmass.clothRoomBackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cofmass.clothRoomBackend.entity.Admin;
import com.cofmass.clothRoomBackend.entity.User;
import com.cofmass.clothRoomBackend.entity.Work;
import com.cofmass.clothRoomBackend.service.UserService;
import com.cofmass.clothRoomBackend.mapper.UserMapper;
import com.cofmass.clothRoomBackend.utils.HttpRequestUtil;
import com.cofmass.clothRoomBackend.vo.UserSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

/**
* @author zlj
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-28 15:35:05
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public User login(String phone, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone)
                .eq(StringUtils.hasLength(password),User::getPassword,password);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getLoginUser() {
        String token = HttpRequestUtil.getRequest().getHeader("token");
        if (token!=null){
            try {
                User o = (User) redisTemplate.opsForValue().get(token);
                return o;
            } catch (NullPointerException n){
                return null;
            }
        }
        return null;
    }

    @Override
    public Page<User> pageUser(int currentPage, int pageSize, UserSearchVo param) {
        Page<User> page = new Page<>(currentPage, pageSize);
        page.setOptimizeCountSql(false);
        if (param != null){
            Page<User> pageUser = page(page, new LambdaQueryWrapper<User>()
                .like(User::getUserName,param.getUserName())
                .like(User::getPhone,param.getPhone())
                .like(User::getGender,param.getGender())
                .like(User::getAddress,param.getAddress()));
            return pageUser;
        }else {
            Page<User> pageUser = page(page);
            return pageUser;
        }
    }

}




