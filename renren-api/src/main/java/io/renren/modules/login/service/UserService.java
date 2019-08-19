/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.entity.PageData;
import io.renren.form.LoginForm;
import io.renren.modules.login.entity.UserEntity;

import java.util.Map;

/**
 * 用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回登录信息
	 */
	Map<String, Object> login(LoginForm form);


	String userLogin(PageData pageData) throws Exception;


	void saveuserwehartinfo(PageData pageData) throws Exception;

    void updateuser(PageData pageData) throws Exception;

	Long selectUserByusername(PageData o) throws Exception;

	PageData selectUserInfoByusername(PageData pageData) throws Exception;

    String add(PageData pageData) throws Exception;
}
