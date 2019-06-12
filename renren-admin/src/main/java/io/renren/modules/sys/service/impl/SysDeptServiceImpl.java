/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.annotation.DataFilter;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public List<SysDeptEntity> queryList(Map<String, Object> params) {
        return baseMapper.queryList(params);
    }

    @Override
    public List<Long> queryDetpIdList(Long parentId) {
        return baseMapper.queryDetpIdList(parentId);
    }

    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        //部门及子部门ID列表
        List<Long> deptIdList = new ArrayList<>();

        //获取子部门ID
        List<Long> subIdList = queryDetpIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    @Override
    public List<PageData> deptlistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.
                findForList("io.renren.modules.sys.dao.SysDeptDao.deptlistPage", page);
    }

    @Override
    public void save(PageData pageData) throws Exception {
        daoSupport.save("io.renren.modules.sys.dao.SysDeptDao.save", pageData);
    }

    @Override
    public void update(PageData pageData) throws Exception {
        daoSupport.update("io.renren.modules.sys.dao.SysDeptDao.update", pageData);
    }

    @Override
    public List<PageData> selectDeptById(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.
                findForList("io.renren.modules.sys.dao.SysDeptDao.selectDeptById", pageData);
    }

    @Override
    public List<PageData> selectAllDept(Long corid) throws Exception {
        return (List<PageData>) daoSupport.
                findForList("io.renren.modules.sys.dao.SysDeptDao.selectAllDept",null);
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
        for (Long deptId : subIdList) {
            List<Long> list = queryDetpIdList(deptId);
            if (list.size() > 0) {
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }
}
