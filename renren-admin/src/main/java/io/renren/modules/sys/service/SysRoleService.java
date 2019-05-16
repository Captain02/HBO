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
import io.renren.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);
	
	void deleteBatch(Long[] roleIds);

    List<PageData> rolelistPage(Page page) throws Exception;

	void update(PageData pageData) throws Exception;

    void save(PageData pageData) throws Exception;

    List<PageData> selectRoleById(PageData pageData) throws Exception;
}
