/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.common.entity.PageData;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(@Param("userId") Long userId,@Param("corid") Long corid);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(@Param("userid")Long userid,@Param("corid")Long corid);

    String geUserPassword(@Param("pagedate") PageData pagedate);

	SysUserEntity selectUserByUsernameCorporaition(@Param("username") String username, @Param("corid") Long corid);

    void removeUserByIds(@Param("list") List<Long> list, @Param("corid") Long corid);


}
