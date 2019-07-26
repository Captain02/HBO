package io.renren.modules.sys.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/add")
    public R save() throws Exception {
        PageData pageData = this.getPageData();
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

    public String Verify(PageData pageData){
        CheckParameterUtil.checkParameterMap(pageData,"name","gender","persionnum","password","confirmPassword","phone","wechat","qq","college","collegetie");
        if(!pageData.getValueOfString("confirmPassword").equals(pageData.getValueOfString("password"))){
            return "两次密码不一致";
        }
        if(!CheckParameterUtil.isMobile(pageData.getValueOfString("phone"))){
            return "手机号不正确";
        }
        if(!CheckParameterUtil.checkQQ(pageData.getValueOfString("qq"))){
            return "QQ格式不正确";
        }
        if(!CheckParameterUtil.checkWechat(pageData.getValueOfString("wechat"))){
            return "微信号格式不正确";
        }
        return "";
    }
}
