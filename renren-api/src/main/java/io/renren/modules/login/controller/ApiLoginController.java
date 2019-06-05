/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.login.controller;


import io.renren.annotation.Login;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.login.service.TokenService;
import io.renren.modules.login.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 登录接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
@Api(tags = "登录接口")
public class ApiLoginController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "登录", tags = {"登录接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query", value = "学号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "password", paramType = "query", value = "密码", required = true, dataType = "string"),
    })
    @PostMapping("/sys/login")
    public R login() throws Exception {
        PageData pageData = this.getPageData();
        String token = userService.userLogin(pageData);
        if (token == null) {
            return R.error(401, "登录失败");
        }
        //表单校验
//        ValidatorUtils.validateEntity(form);

//        //用户登录
//        Map<String, Object> map = userService.login(form);

        return R.ok().put("token", token);
    }

    @ApiOperation(value = "刷新token,请求头带上Authorization即可", tags = {"登录接口"})
    @Login
    @GetMapping("/sys/refreshToken")
    public R refresh(HttpServletRequest req) {
        String tokenReq = req.getHeader("Authorization");
        String username = JWTUtil.getUsername(tokenReq);
        String uuidPassword = UUID.randomUUID().toString().replace("-", "");
        String newToken = JWTUtil.sign(username, uuidPassword);
        redisUtils.set(username, newToken, 60 * 60 * 24);
        return R.ok().put("token", newToken);
    }

    @ApiOperation(value = "退出", tags = {"登录接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query", value = "学号", required = true, dataType = "string")
    })
    @Login
    @PostMapping("/logout")
    public R logout() {
        PageData pageData = this.getPageData();
        redisUtils.set(pageData.getValueOfString("username"), "", 0);
        return R.ok();
    }


}
