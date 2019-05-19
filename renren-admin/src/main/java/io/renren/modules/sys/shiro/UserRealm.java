/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.Constant;
import io.renren.common.utils.JWTUtil;
import io.renren.modules.sys.dao.SysMenuDao;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysMenuEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import javafx.print.PaperSource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken auth) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) auth;
        String token = (String) jwtToken.getCredentials();
        String username = null;
        // 解密获得username，用于和数据库进行对比
        if (!StringUtils.isEmpty(token)) {
            username = JWTUtil.getUsername(token);
        }
        if (!StringUtils.isEmpty(jwtToken.getUsername())) {
            SysUserEntity user = sysUserDao.selectUserByUsernameCorporaition(jwtToken.getUsername(),jwtToken.getCorid());
            //账号锁定
            verifyUser(user);
            if (user.getPassword().equals(ShiroUtils.sha256(jwtToken.getPassword(),user.getSalt()))) {
                String signToken = JWTUtil.sign(jwtToken.getUsername(), ShiroUtils.sha256(jwtToken.getPassword(),user.getSalt()));
                user.setToken(signToken);
                return new SimpleAuthenticationInfo(user, jwtToken.getPassword(), getName());
            }

        } else if (username == null) {
            throw new AuthenticationException("token invalid");
        }

//		SysUserEntity userBean = userService.getUser(username);
        SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", username));

        verifyUser(user);


        //验证是否过期
        String password = user.getPassword();
        if (!JWTUtil.verify(token, username, password)) {
            throw new AuthenticationException("Username or password error");
        }


//        SysUserEntity isuser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
//        if (isuser==null){
//            throw new AuthenticationException("token invalid");
//        }
        //重复登录验证
        return new SimpleAuthenticationInfo(user, token, getName());

//		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
//
//		//查询用户信息
//		SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", token.getUsername()));
//		//账号不存在
//		if(user == null) {
//			throw new UnknownAccountException("账号或密码不正确");
//		}
//
//
//
//		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
//		return info;
    }


    private void verifyUser(SysUserEntity user){
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        //账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

    }


}
