package io.renren.modules.userinfo.controller;

import io.renren.comm.util.JsonUtils;
import io.renren.comm.util.ShiroUtils;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.login.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
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
    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "用户认证保存的信息", tags = {"个人信息"}, notes = "{'nickName':姓名,'gender':1男/0女}")
    @PostMapping(value = "/saveuserwehartinfo", produces = "application/json;charset=utf-8")
    public R saveuserwehartinfo(@RequestBody String json) throws Exception {
        Map o = JsonUtils.parseStringToObject(json, Map.class);
        PageData pageData = new PageData();
        pageData.put("name", (String) o.get("nickName"));
        Integer gender = (Integer) o.get("gender");
        pageData.put("gender", gender == 1 ? "男" : "女");
        pageData.put("fileid", gender == 1 ? 7 : 8);
        userService.saveuserwehartinfo(pageData);

        return R.ok().put("data", pageData);
    }

    @ApiOperation(value = "用户信息修改（除了学号和密码其他的信息）", tags = {"个人信息"}, notes = "" +
            "'email':邮箱," +
            "'mobile':手机," +
            "'wechart':微信," +
            "'qq':qq," +
            "'college':学院," +
            "'collegetie':专业," +
            "'descs':描述," +
            "'name':姓名," +
            "}")
    @PostMapping(value = "/updateuser", produces = "application/json;charset=utf-8")
    public R updateuser(@RequestBody String json) throws Exception {
        PageData o = JsonUtils.parseStringToObject(json, PageData.class);
        CheckParameterUtil.checkParameterMap(o, "user_id");
        userService.updateuser(o);
        return R.ok();
    }


    @ApiOperation(value = "用户信息修改（学号和密码）", tags = {"个人信息"}, notes = "" +
            "{'username':学号," +
            "'password':密码," +
            "}")
    @PostMapping(value = "/updateUsernameAndPassword", produces = "application/json;charset=utf-8")
    public R updateUsernameAndPassword(@RequestBody String json) throws Exception {
        PageData o = JsonUtils.parseStringToObject(json, PageData.class);
        try {
            CheckParameterUtil.checkParameterMap(o, "username", "password", "user_id");
        }catch (Exception e){
            return R.error("学号密码不能为空");
        }
        String username = (String) o.get("username");
        Long num = userService.selectUserByusername(o);
        String token = null;
        if (num > 0) {
            return R.error("学号重复");
        }
        String oldpassword = (String) o.get("password");
        String salt = RandomStringUtils.randomAlphanumeric(20);
        o.put("salt", salt);
        String newpassword = ShiroUtils.sha256(oldpassword, salt);
        o.put("newpassword", newpassword);
        token = JWTUtil.sign(username, oldpassword);
        redisUtils.set(username, token, 60 * 60 * 1);


        userService.updateuser(o);
        return R.ok().put("token", token);
    }

    //登录界面用户注册
    @ApiOperation(value = "用户信息修改（除了学号和密码其他的信息）", tags = {"个人信息"}, notes = "" +
            "'mobile':手机," +
            "'gender':行吧," +
            "'name':姓名," +
            "'password':密码," +
            "'username':学号," +
            "}")
    @PostMapping("/addPersion")
    public R save(@RequestBody String json) throws Exception {
//        PageData pageData = this.getPageData();
        PageData pageData = JsonUtils.parseStringToObject(json, PageData.class);
        Long aLong = userService.selectUserByusername(pageData);
        if (aLong != 0){
            return R.error("已存在该用户");
        }
        String gender = pageData.getValueOfString("gender");
        if (gender.equals("男")){
            pageData.put("fileid",7);
        }else {
            pageData.put("fileid",8);
        }
//        String msg = Verify(pageData);
//        if(msg.length()>0){
//            return R.error(msg);
//        }
//        Boolean isSave = sysUserService.selectUserByPersionnum(pageData);
//        if (!isSave) {
//            return R.error("用户已存在");
//        }
        userService.add(pageData);
        return R.ok();
    }

    public String Verify(PageData pageData){
        CheckParameterUtil.checkParameterMap(pageData,"name","gender","username","password","mobile","wechart","qq","college","collegetie");
//        if(!pageData.getValueOfString("confirmPassword").equals(pageData.getValueOfString("password"))){
//            return "两次密码不一致";
//        }
        if(!CheckParameterUtil.isMobile(pageData.getValueOfString("mobile"))){
            return "手机号格式不正确";
        }
        if(!CheckParameterUtil.checkQQ(pageData.getValueOfString("qq"))){
            return "QQ格式不正确";
        }
        if(!CheckParameterUtil.checkWechat(pageData.getValueOfString("wechart"))){
            return "微信号格式不正确";
        }
        return "";
    }
}
