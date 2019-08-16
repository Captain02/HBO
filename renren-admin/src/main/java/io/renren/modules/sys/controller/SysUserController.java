/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;


import io.renren.common.commBusiness.commService.CaptchaService;
import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.commBusiness.commService.MailService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.exception.RRException;
import io.renren.common.util.Tools;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.common.validator.Assert;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private CommService commService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MailService mailService;
    @Autowired
    private CaptchaService captchaService;


    /**
     * 所有用户列表
     */

//	@ApiOperation(value = "用户列表", tags = {"用户"})
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "Integer",paramType = "query",name = "pn",value = "当前页",required = true),
//            @ApiImplicitParam( dataType = "Integer",paramType = "query",name = "size",value = "当前页大小",required = true)
//    })
    @GetMapping("/list")
//    @RequiresPermissions("sys:user:list")
    public R list() throws Exception {
        Page page = new Page();
        PageData pageData = this.getPageData();
        page.setPageSize(pageData.getValueOfInteger("size"));
        page.setCurrPage(pageData.getValueOfInteger("page"));
        page.setPd(pageData);
        //PageUtils page = sysUserService.queryPage(params);
        List<PageData> list = sysUserService.userlistPage(page);

        return R.ok().put("page", page).put("data", list);
    }

    @GetMapping("/getUsersByName")
    public R getUsersByName() throws Exception {
        Page page = new Page();
        PageData pageData = this.getPageData();
        page.setPageSize(9999);
        page.setCurrPage(1);
        pageData.put("username", "");
        page.setPd(pageData);
        //PageUtils page = sysUserService.queryPage(params);
        List<PageData> list = sysUserService.userlistPage(page);

        return R.ok().put("page", page).put("data", list);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/infoById")
    public R info() throws Exception {
        PageData pageData = this.getPageData();
        PageData data = sysUserService.getinfoByid(pageData);
        //return R.ok().put("user", getUser());
        return R.ok().put("data", data);
    }

    /**
     * 修改登录用户密码
     */
//    @SysLog("修改密码")
    @PostMapping("/password")
    public R password() throws Exception {
        PageData pageData = this.getPageData();
        String newPassword = pageData.getValueOfString("newPassword");
        Assert.isBlank(newPassword, "新密码不为能空");
        PageData user = sysUserService.getinfoByid(pageData);
        //原密码
        String password = ShiroUtils.sha256(pageData.getValueOfString("password"), user.getValueOfString("salt"));
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, user.getValueOfString("salt"));
        //更新密码
        boolean flag = sysUserService.
                updatePassword(Long.parseLong(user.getValueOfString("user_id")), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info")
    public R info(HttpServletRequest req) throws Exception {
        String tokenreq = req.getHeader("Authorization");
        String username = JWTUtil.getUsername(tokenreq);
        PageData pageData = this.getPageData();
        pageData.put("username", username);
        PageData user = sysUserService.selectUserByUsername(pageData);
        //获取用户所属的角色列表
        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     * , String roles
     */
    @RequestMapping("/save")
    public R save(SysUserEntity user, Long corid) throws Exception {
        PageData pageData = this.getPageData();
        SysUserEntity isSave = sysUserService.selectUserByUsernameCorporaition(pageData);
        if (isSave != null) {
            sysUserService.saveUserCor(user, corid);
            return R.ok();
        }
        return R.error(503,"用户不存在");
//        PageData userinfo = sysUserService.selectUserByUsername(pageData);
//        if (userinfo != null) {
//            sysUserService.saveUserCor(user, corid);
//            return R.ok();
//        } else {
//            user.setPersionnum(user.getUsername());
//            sysUserService.saveUser(user, corid);
//            return R.ok();
//        }
    }

    /*
     *扫码添加用户
     * */
    @PostMapping("/QRSave")
    public R QRSave() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "username", "password");
        List<PageData> list = sysUserService.selectCorByUserCorid(pageData);
        if (list.size()>0){
            return R.error("用户已提交申请");
        }
        PageData user = sysUserService.selectUserByUsername(pageData);
        if (user!=null){
            pageData.put("userid", user.getValueOfInteger("user_id"));
            pageData.put("corid", pageData.getValueOfInteger("corid"));
            sysUserService.saveExiceUserCOR(pageData);
        }else {
            sysUserService.QRSave(pageData);
        }
        return R.ok();
    }

    /**
     * 修改用户
     */
//    @SysLog("修改用户")
    @PostMapping("/update")
    public R update(SysUserEntity user) throws Exception {

        sysUserService.update(user);

        return R.ok();
    }

    //修改用户基本信息
    @PostMapping("/updateUserInfo")
    public R updateUserInfo() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "userId");
        sysUserService.updateUserInfo(pageData);
        return R.ok();
    }

    /**
     * 上传头像
     *
     * @return
     */
    @PostMapping("/chatHead")
    @ApiOperation(value = "上传头像", notes = "上传头像", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "chatHead", value = "图片文件", required = true, dataType = "String")
    public R chatHead(@RequestParam("chatHead") MultipartFile chatHead, HttpServletRequest request) {
        System.out.println("上传头像");
        //文件上传
        PageData pageData = this.getPageData();
//        pageData.put("chatHead",chatHead);
//        CheckParameterUtil.checkParameterMap(pageData,"chatHead");
        String path = commService.uploadFile(chatHead, request, "/file/chatHead/");
        if (path == null) {
            return R.error("文件上传失败");
        }
        //保存到数据库
        pageData.put("url", path);
        pageData.put("fileName", chatHead.getOriginalFilename());
        try {
            sysUserService.save(pageData);
            return R.ok("上传头像成功").put("data", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存头像失败");
        }
    }

    /**
     * 删除头像
     *
     * @return
     */
    @PostMapping("/delChatHead")
    @ApiOperation(value = "删除头像", notes = "删除头像", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "delChatHead", value = "删除头像", required = true, dataType = "String")
    public R delChatHead(HttpServletRequest request) {
        System.out.println("删除头像");
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, new String[]{"url", "id"});

        if (commService.deleteFile(pageData.get("url").toString()) && sysUserService.delChatHead(pageData)) {
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 删除用户
     */
//    @SysLog("删除用户")
    @RequestMapping("/delete")
    public R delete(@RequestParam("userIds") String userIds, @RequestParam("corid") Long corid) throws Exception {
        String[] strings = Tools.str2StrArray(userIds, ",");
        List<Long> collect = Arrays.stream(strings).map(x -> Long.parseLong(x)).collect(Collectors.toList());
//        Long[] array = collect.stream().toArray(Long[]::new);
//
//        if(ArrayUtils.contains(array, 1L)){
//			return R.error("系统管理员不能删除");
//		}
//
//		if(ArrayUtils.contains(array, getUserId())){
//			return R.error("当前用户不能删除");
//		}
        sysUserService.removeUserByIds(collect, corid);

        return R.ok();
    }

    //修改部门
    @PostMapping("/updataUserDept")
    public R updataUserDept() throws Exception {
        PageData pageData = this.getPageData();
        PageData i = sysUserService.selectUserDept(pageData);
        if (i.getValueOfInteger("num") != 0) {
            sysUserService.updataUserDept(pageData);
        } else {
            sysUserService.saveUserDept(pageData);
        }

        return R.ok();
    }

    //修改角色
    @PostMapping("/updateUserRole")
    public R updataUserRole() throws Exception {
        PageData pageData = this.getPageData();
        if (pageData.getValueOfInteger("isRole") == 1) {
            sysUserService.insertUserRole(pageData);
        } else {
            sysUserService.deleUserRole(pageData);
        }

        return R.ok();
    }

    //获得角色所有权限
    @GetMapping("/getUserPermission")
    public R getRolePermission() throws Exception {
        PageData pageData = this.getPageData();
        List<PageData> pageDataList = sysUserService.getUserPermission(pageData);
        return R.ok().put("data", pageDataList);
    }

    //获得与用户权限的Stirng列表
    @GetMapping("/getLoginUserPermission")
    public R getLoginUserPermission() throws Exception {
        PageData pageData = this.getPageData();
        List<String> pageDataList = sysUserService.queryAllPerms(pageData);
        Set<String> permsSet = new HashSet<>();
        for (String perms : pageDataList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim()));
        }
        return R.ok().put("data", permsSet);
    }

    //按照用户名查询用户所有的社团
    @GetMapping("/getUserCors")
    public R getUserCors() throws Exception {
        PageData pageData = this.getPageData();
        List<PageData> data = sysUserService.getUserCorsByUserName(pageData);
        return R.ok().put("data", data);
    }

    //用户名是否重复
    @PostMapping("/selectCountByUserName")
    public R selectCountByUserName() throws Exception {
        PageData pageData = this.getPageData();
        Long count = sysUserService.selectCountByUserName(pageData);
        if (count != 0) {
            return R.error(504, "用户名存在");
        }
        return R.ok();
    }

    //查询用户是否加入社团或者或者用户是否存在
    @PostMapping("/getCheckUserCor")
    public R getCheckUserCor() throws Exception {
        PageData pageData = this.getPageData();
        List<PageData> list = sysUserService.selectCorByUserCorid(pageData);
        if (list.size() != 0) {
            return R.error(505, "该账户重复注册社团");
        }
        return R.ok();
    }

    //根据用户名获取手机号和邮箱
    @PostMapping("/getUserInfo")
    public R getUserInfo() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "username","captcha","key");
        //获取redis中的验证码
        String trueCaptcha = redisUtils.get(pageData.getValueOfString("key"));
        if (trueCaptcha == null || "".equals(trueCaptcha)) {
            return R.error("验证码已失效");
        }
        if(!pageData.getValueOfString("captcha").equals(trueCaptcha)){
            return R.error("验证码不正确");
        }
        Long count = sysUserService.selectCountByUserName(pageData);
        if (count == 0) {
            return R.error("用户名不存在");
        }
        pageData = sysUserService.selectEmailAndPhoneByUserName(pageData);
        PageData data = new PageData();
        data.put("email",pageData.getValueOfString("email"));
        data.put("mobile",pageData.getValueOfString("mobile"));
        data.put("username",pageData.getValueOfString("username"));
        return R.ok().put("data", data);
    }

    //发送验证码
    @PostMapping("/sendVerCode")
    public R sendVerCode() throws MessagingException {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "username", "type", "checkway");
        //邮箱验证：0，手机验证：1
        if (pageData.getValueOfInteger("type") == 0) {
            //验证码生成
            String verCode = String.valueOf((Math.random() * 9 + 1) * 100000).substring(0, 5);
            //将生成的验证码放到redis中
            redisUtils.set("changepassword" + pageData.getValueOfString("username"), verCode, 60 * 10);
            //发送邮件
            String subject = "找回密码邮箱验证";
            StringBuffer content = new StringBuffer();
            content.append("<html><head><title></title></head><body>");
            content.append("您好：" + pageData.getValueOfString("username") + "!<br/>");
            content.append("您的验证码是：").append(verCode).append("<br/>");
            content.append("您可以复制此验证码并返回至百团争鸣社团管理系统，以验证您的邮箱。<br/>")
                    .append("此验证码只能使用一次，在10分钟内有效。验证成功则自动失效。<br/>")
                    .append("如果您没有进行上述操作，请忽略此邮件。");
            mailService.sendHtmlMail(pageData.getValueOfString("checkway"), subject, content.toString());
            //返回
            return R.ok().put("data", pageData);
        }
        if (pageData.getValueOfInteger("type") == 1) {
            return R.ok().put("data", pageData);
        }
        return R.error("参数type为空");
    }

    //校验验证码
    @PostMapping("/checkVerCode")
    public R checkVerCode() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "username", "verCode");
        //获取redis中的验证码
        String redisVerCode = redisUtils.get("changepassword" + pageData.getValueOfString("username"));
        if (redisVerCode == null || "".equals(redisVerCode)) {
            return R.error("验证码已过期");
        }
        if (!redisVerCode.equals(pageData.getValueOfString("verCode"))) {
            return R.error("验证码不正确");
        }
        return R.ok().put("data", pageData);
    }

    //重置密码
    @PostMapping("/resetPwd")
    public R resetPwd() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"password","username");
        PageData user = sysUserService.selectEmailAndPhoneByUserName(pageData);
        pageData.put("userId",user.getValueOfInteger("userId"));
        pageData.put("password",ShiroUtils.sha256(pageData.getValueOfString("password"),user.getValueOfString("salt")));
        sysUserService.updateUserInfo(pageData);
        return R.ok();
    }

    @GetMapping("/kaptcha.jpg")
    public R captcha(HttpServletResponse response) throws IOException {
        PageData captcha = captchaService.captcha(response);
        return R.ok().put("data",captcha);
    }

    //登录界面用户注册
    @PostMapping("/add")
    public R save() throws Exception {
        PageData pageData = this.getPageData();
        String gender = pageData.getValueOfString("gender");
        if (gender.equals("男")){
            pageData.put("fileid",7);
        }else {
            pageData.put("fileid",8);
        }
        String msg = Verify(pageData);
        if(msg.length()>0){
            return R.error(msg);
        }
//        Boolean isSave = sysUserService.selectUserByPersionnum(pageData);
//        if (!isSave) {
//            return R.error("用户已存在");
//        }
        sysUserService.add(pageData);
        return R.ok();
    }

    /**
     * 校验字段
     * @param pageData
     * @return
     */
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

    @GetMapping("/getusersByusernameAndcor")
    @ApiOperation(value = "根据社团id和学号获取个户", notes = "用户", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "学号", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "corid", value = "社团id", required = true, dataType = "Integer",paramType = "query"),
    })
    public R getusersByusernameAndcor() throws Exception {
        PageData pageData = this.getPageData();
        List<PageData> data = sysUserService.getusersByusernameAndcor(pageData);

        return R.ok().put("data",data);
    }
}
