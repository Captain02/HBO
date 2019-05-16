/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user,Long corid) throws Exception;
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user) throws Exception;

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

    String geUserPassword(PageData username);

    List<PageData> userlistPage(Page page) throws Exception;

    void removeUserByIds(List<Long> collect) throws Exception;

	PageData getinfoByid(PageData pageData) throws Exception;

	PageData selectUserByUsername(PageData username) throws Exception;

	void updataUserDept(PageData pageData);

	void save(PageData pageData) throws Exception;
}
