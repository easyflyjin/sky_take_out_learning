package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j  
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties wxProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用接口，获取openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",wxProperties.getAppid());
        map.put("secret",wxProperties.getSecret());
        map.put("grant_type","authorization_code");
        map.put("js_code", userLoginDTO.getCode());
        String jsCode = HttpClientUtil.doGet(WX_LOGIN_URL,map);

        JSONObject jsonObject = JSONObject.parseObject(jsCode);
        String openid = jsonObject.getString("openid");

        //判断OpenID是否为空，空则抛出异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户，是则自动注册
        User user = userMapper.getUserById(openid);
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.setNonExistUser(user);
        }
        //返回User
        return user;
    }
}
