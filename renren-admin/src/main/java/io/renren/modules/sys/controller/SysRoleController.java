/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;

import io.renren.common.annotation.SysLog;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Tools;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.SysRoleEntity;
import io.renren.modules.sys.service.SysRoleDeptService;
import io.renren.modules.sys.service.SysRoleMenuService;
import io.renren.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	
	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(Page page) throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        CheckParameterUtil.checkParameterMap(pageData,"corId");
        //查询
        page.setPd(pageData);
        List<PageData> list = sysRoleService.rolelistPage(page);

        return R.ok().put("page", page).put("data", list);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
		List<SysRoleEntity> list = sysRoleService.list();
		
		return R.ok().put("list", list);
	}

	/**
	 * 根据角色id查询角色
	 * @return
	 */
	@GetMapping("/selectRoleById")
	@RequiresPermissions("sys:dept:select")
	public R selectRoleById() throws Exception {
		//接收并校验参数
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		PageData pageData = new PageData(request);
		String[] parameters = {"roleId"};
		CheckParameterUtil.checkParameterMap(pageData,parameters);
		//查询
		List<PageData> list = sysRoleService.selectRoleById(pageData);

		return R.ok().put("data", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);
		
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		//查询角色对应的部门
		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
		role.setDeptIdList(deptIdList);
		
		return R.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save() throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        String[] parameters = {"roleName","corId","remark"};
        CheckParameterUtil.checkParameterMap(pageData,parameters);
        //插入
        sysRoleService.save(pageData);

        return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@PostMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update() throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        CheckParameterUtil.checkParameterMap(pageData,"roleId");
        //更新
        sysRoleService.update(pageData);
        return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@GetMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete() throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        CheckParameterUtil.checkParameterMap(pageData,"roleId");
        //判断是否是批量删除
        String[] strings = Tools.str2StrArray(pageData.get("roleId").toString(), ",");
        for(int i=0; i<strings.length; i++){
            pageData.put("delFlag",-1);
            pageData.put("roleId",strings[i]);
            //删除
            sysRoleService.update(pageData);
        }

        return R.ok();
	}

	//修改角色权限
	@PostMapping("/updateRolePermission")
	public R updateRolePermission(HttpServletRequest request) throws Exception {
		PageData pageData = new PageData(request);
		if (pageData.getValueOfInteger("isRolePermission")==0){
			sysRoleService.deleteRolePermission(pageData);
		}else {
			sysRoleService.saveRolePermission(pageData);
		}
		return R.ok();
	}
}
