/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Tools;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.Constant;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     */
    @ApiOperation(value = "用户列表", tags = {"用户"})
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "Integer", paramType = "query", name = "currPage", value = "当前页", required = true),
            @ApiImplicitParam(dataType = "Integer", paramType = "query", name = "pageSize", value = "每页显示记录数", required = true)
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("sys:dept:list")
    public R list(Page page) throws Exception {
//		System.out.println("进入list。。");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData,"corId");
        //查询
        page.setPd(pageData);
        List<PageData> list = sysDeptService.deptlistPage(page);

        return R.ok().put("page", page).put("data", list);
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:dept:list")
    public R info() {
        long deptId = 0;
        if (getUserId() != Constant.SUPER_ADMIN) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
            Long parentId = null;
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (parentId == null) {
                    parentId = sysDeptEntity.getParentId();
                    continue;
                }

                if (parentId > sysDeptEntity.getParentId().longValue()) {
                    parentId = sysDeptEntity.getParentId();
                }
            }
            deptId = parentId;
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.getById(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save() throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        String[] parameters = {"parentId","name","corId"};
        CheckParameterUtil.checkParameterMap(pageData,parameters);
        //插入
        sysDeptService.save(pageData);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update() throws Exception {
//        sysDeptService.updateById(dept);
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        CheckParameterUtil.checkParameterMap(pageData,"deptId");
        //更新
        sysDeptService.update(pageData);
        return R.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete() throws Exception {
        //接收并校验参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        PageData pageData = new PageData(request);
        CheckParameterUtil.checkParameterMap(pageData,"deptId");
        //判断是否是批量删除
        String[] strings = Tools.str2StrArray(pageData.get("deptId").toString(), ",");
        for(int i=0; i<strings.length; i++){
            //判断是否有子部门
            List<Long> deptList = sysDeptService.queryDetpIdList(Long.parseLong(strings[i]));
            if (deptList.size() > 0) {
                return R.error("请先删除子部门");
            }
            pageData.put("delFlag",-1);
            pageData.put("deptId",strings[i]);
            //删除
            sysDeptService.update(pageData);
        }

        return R.ok();
    }

}
