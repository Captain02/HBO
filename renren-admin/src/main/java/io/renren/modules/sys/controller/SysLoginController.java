/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.IsVerificationService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.service.TitleService;
import io.renren.modules.sys.shiro.JWTToken;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
public class SysLoginController extends BaseController {
    @Autowired
    private Producer producer;
    @Autowired
    private TitleService titleService;
    @Autowired
    private IsVerificationService isVerificationService;
    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        //ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }


    @ResponseBody
    @RequestMapping(value = "/sys/getTitleName", method = RequestMethod.POST)
    public R getTitleName(String username) {
        String verification = isVerificationService.getIsVerification();
        String titleName = titleService.getTitleName();

        return R.ok().put("titleName", titleName).put("verification", verification);
    }

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha, Long corid) {
        String titleName = isVerificationService.getIsVerification();
        if (titleName.equals("是")) {
            //String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
//			if(!captcha.equalsIgnoreCase(kaptcha)){
//				return R.error("验证码不正确");
//			}
        }

        try {
            Subject subject = ShiroUtils.getSubject();
            JWTToken token = new JWTToken(username, password,corid);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok().put("token", ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getToken()).put("corId",corid);
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        //ShiroUtils.logout();
        return "redirect:login.html";
    }

    @ResponseBody
    @RequestMapping(value = "/sys/refreshToken", method = RequestMethod.POST)
    public R refreshToken(HttpServletRequest req) {
        String tokenreq = req.getHeader("Authorization");
        String username = JWTUtil.getUsername(tokenreq);
		PageData pageData = new PageData();
		pageData.put("username",username);
		String password = sysUserService.geUserPassword(pageData);
        return R.ok().put("token", JWTUtil.sign(username, password));
    }

}
