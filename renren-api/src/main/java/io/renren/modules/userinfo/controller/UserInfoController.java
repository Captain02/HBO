package io.renren.modules.userinfo.controller;

import io.renren.comm.util.JsonUtils;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.login.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api(tags = {"个人信息"})
@RequestMapping("/sys/userinfo")
public class UserInfoController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "用户认证保存的信息",tags = {"个人信息"},notes = "{'name':姓名,'gender':1男/0女}")
    @PostMapping(value = "/saveuserwehartinfo", produces = "application/json;charset=utf-8")
    public R saveuserwehartinfo(@RequestBody String json) throws Exception {
        Map o = JsonUtils.parseStringToObject(json, Map.class);
        PageData pageData = new PageData();
        pageData.put("name",(String)o.get("nickName"));
        Integer gender = (Integer) o.get("gender");
        pageData.put("gender",gender==1?"男":"女");
        pageData.put("fileid",gender==1?7:8);
        userService.saveuserwehartinfo(pageData);

        return R.ok().put("data",pageData);
    }
}
