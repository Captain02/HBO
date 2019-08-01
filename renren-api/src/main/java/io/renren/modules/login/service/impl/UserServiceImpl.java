/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.login.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.comm.util.ShiroUtils;
import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.common.exception.RRException;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.RedisUtils;
import io.renren.common.validator.Assert;
import io.renren.form.LoginForm;
import io.renren.modules.login.dao.UserDao;
import io.renren.modules.login.entity.TokenEntity;
import io.renren.modules.login.entity.UserEntity;
import io.renren.modules.login.service.TokenService;
import io.renren.modules.login.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DaoSupport daoSupport;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public UserEntity queryByMobile(String mobile) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("mobile", mobile));
    }

    @Override
    public Map<String, Object> login(LoginForm form) {
        UserEntity user = queryByMobile(form.getMobile());
        Assert.isNull(user, "手机号或密码错误");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))) {
            throw new RRException("手机号或密码错误");
        }

        //获取登录token
        TokenEntity tokenEntity = tokenService.createToken(user.getUserId());

        Map<String, Object> map = new HashMap<>(2);
        map.put("token", tokenEntity.getToken());
        map.put("expire", tokenEntity.getExpireTime().getTime() - System.currentTimeMillis());

        return map;
    }

    @Override
    public String userLogin(PageData pageData) throws Exception {
        PageData forObject = (PageData) daoSupport.
                findForObject("io.renren.modules.login.dao.UserDao.userLogin", pageData);
        if (forObject != null) {
            String password = pageData.getValueOfString("password");
            String passWordSha256 = ShiroUtils.sha256(password, forObject.getValueOfString("salt"));
            if (passWordSha256.equals(forObject.getValueOfString("password"))) {
                String token = JWTUtil.sign(forObject.getValueOfString("username"), password);
                redisUtils.set(pageData.getValueOfString("username"), token, 60 * 60 * 1);
                return token;
            }
        }

        return null;
    }

    @Override
    public void saveuserwehartinfo(PageData pageData) throws Exception {
        daoSupport.save("io.renren.modules.login.dao.UserDao.saveuserwehartinfo",pageData);
    }

    @Override
    public void updateuser(PageData pageData) throws Exception {
        daoSupport.update("io.renren.modules.login.dao.UserDao.updateUser",pageData);
    }

    @Override
    public Long selectUserByusername(PageData o) throws Exception {
        return (Long) daoSupport.findForObject("io.renren.modules.login.dao.UserDao.selectUserByusername",o);
    }
}
