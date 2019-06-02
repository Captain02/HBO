/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;


import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Tools;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.common.validator.Assert;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            return R.error("用户已存在");
        }
        PageData userinfo = sysUserService.selectUserByUsername(pageData);
        if (userinfo != null) {
            sysUserService.saveUserCor(user, corid);
            return R.ok();
        } else {
            user.setPersionnum(user.getUsername());
            sysUserService.saveUser(user, corid);
            return R.ok();
        }
    }

    /*
     *扫码添加用户
     * */
    @PostMapping("/QRSave")
    public R QRSave() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "username", "password");
        sysUserService.QRSave(pageData);
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
        return R.ok().put("data",data);
    }

    //用户名是否重复
    @GetMapping("/selectCountByUserName")
    public R selectCountByUserName() throws Exception {
        PageData pageData = this.getPageData();
        Long count = sysUserService.selectCountByUserName(pageData);
        if (count!=0){
            return R.error(504,"用户名存在");
        }
        return R.ok();
    }

}
