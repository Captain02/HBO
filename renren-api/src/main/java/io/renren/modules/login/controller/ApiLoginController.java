/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.login.controller;


import io.renren.annotation.Login;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.form.LoginForm;
import io.renren.modules.login.service.TokenService;
import io.renren.modules.login.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * 登录接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
@Api(tags="登录接口")
public class ApiLoginController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    RedisUtils redisUtils;

    @PostMapping("/sys/login")
    @ApiOperation("登录")
    public R login() throws Exception {
        PageData pageData = this.getPageData();
        String token = userService.userLogin(pageData);
        if (token == null){
            return R.error(401,"登录失败");
        }
        //表单校验
//        ValidatorUtils.validateEntity(form);

//        //用户登录
//        Map<String, Object> map = userService.login(form);

        return R.ok().put("token",token);
    }

    @Login
    @GetMapping("/sys/refreshToken")
    public R refresh(HttpServletRequest req){
        String tokenReq = req.getHeader("Authorization");
        String username = JWTUtil.getUsername(tokenReq);
        String uuidPassword = UUID.randomUUID().toString().replace("-","");
        String newToken = JWTUtil.sign(username, uuidPassword);
        redisUtils.set(username,newToken,60 * 60 * 1);
        return R.ok().put("token",newToken);
    }

    @Login
    @PostMapping("/logout")
    @ApiOperation("退出")
    public R logout(@ApiIgnore @RequestAttribute("userId") long userId){
        tokenService.expireToken(userId);
        return R.ok();
    }



}
