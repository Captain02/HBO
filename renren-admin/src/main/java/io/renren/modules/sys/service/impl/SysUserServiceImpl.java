/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.annotation.DataFilter;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<Long> queryAllMenuId(Long userId, Long corid) {
        return baseMapper.queryAllMenuId(userId, corid);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        for (SysUserEntity sysUserEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            sysUserEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity user, Long corid) throws Exception {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        this.save(user);

        PageData pageData = new PageData();
        pageData.put("userid", user.getUserId());
        pageData.put("corid", corid);
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.saveUserCor", pageData);
//		pageData.put("deptid", user.getDeptId());
//		daoSupport.save("io.renren.modules.sys.dao.SysUserDao.saveUserDept",pageData);
        //保存用户与角色关系
//        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserEntity user) throws Exception {
        daoSupport.update("io.renren.modules.sys.dao.SysUserDao.updateUserByid", user);
    }


    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    @Override
    public String geUserPassword(PageData pagedate) {
        return sysUserDao.geUserPassword(pagedate);
    }

    @Override
    public List<PageData> userlistPage(Page page) throws Exception {
        List<PageData> list = (List<PageData>) daoSupport.
                findForList("io.renren.modules.sys.dao.SysUserDao.userlistPage", page);
        return list;
    }

    @Override
    public void removeUserByIds(List<Long> list, Long corid) {
        //daoSupport.batchUpdateBylist("io.renren.modules.sys.dao.SysUserDao.removeUserByIds", list);
        sysUserDao.removeUserByIds(list, corid);
    }

    @Override
    public void saveUserCor(SysUserEntity user, Long corid) throws Exception {
        PageData pageData = new PageData();
        pageData.put("userid", user.getUserId());
        pageData.put("corid", corid);
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.saveUserCor", pageData);
    }

    @Override
    public SysUserEntity selectUserByUsernameCorporaition(PageData pageData) throws Exception {
        return (SysUserEntity) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectUserByUsernameCorporaition", pageData);
    }

    @Override
    public PageData selectByOpenid(PageData data) throws Exception {
        return (PageData) daoSupport.
                findForObject("io.renren.modules.sys.dao.SysUserDao.selectByOpenid", data);
    }

    @Override
    public void insertUserCor(PageData usercor) throws Exception {
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.insertUserCor", usercor);
    }

    @Override
    public void QRSave(PageData pageData) throws Exception {
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        pageData.put("salt", salt);
        pageData.put("persionnum", pageData.getValueOfString("username"));
        String password = pageData.getValueOfString("password");
        pageData.put("password", ShiroUtils.sha256(password, salt));

        if (pageData.getValueOfString("gender").equals("男")) {
            pageData.put("fileid", 7);
        } else {
            pageData.put("fileid", 8);
        }
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.QRSave", pageData);

        pageData.put("userid", pageData.getValueOfInteger("user_id"));
        pageData.put("corid", pageData.getValueOfInteger("corid"));
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.QRsaveUserCor", pageData);
    }

    @Override
    public List<String> queryAllPerms(PageData pageData) throws Exception {
        return (List<String>) daoSupport.findForList("io.renren.modules.sys.dao.SysUserDao.queryAllPerms", pageData);
    }

    @Override
    public List<PageData> selectCorByUserCorid(PageData usercor) throws Exception {
        return (List<PageData>) daoSupport.findForList("io.renren.modules.sys.dao.SysUserDao.selectCorByUserCorid", usercor);
    }

    @Override
    public List<PageData> getUserCorsByUserName(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("io.renren.modules.sys.dao.SysUserDao.getUserCorsByUserName", pageData);
    }

    @Override
    public Long selectCountByUserName(PageData pageData) throws Exception {
        return (Long) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectCountByUserName", pageData);
    }


    @Override
    public PageData getinfoByid(PageData pageData) throws Exception {
        PageData forObject = (PageData) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.getinfoByid", pageData);
        return forObject;
    }

    @Override
    public PageData selectUserByUsername(PageData username) throws Exception {
        PageData forObject = (PageData) daoSupport.
                findForObject("io.renren.modules.sys.dao.SysUserDao.selectUserByUsername", username);
        return forObject;
    }

    @Override
    public void updataUserDept(PageData pageData) throws Exception {
        daoSupport.update("io.renren.modules.sys.dao.SysUserDao.updateUserDept", pageData);

    }

    @Override
    public PageData save(PageData pageData) throws Exception {
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.save", pageData);
        return pageData;
    }

    @Override
    public boolean delChatHead(PageData pageData) {
        //删除数据库中的字段
        try {
            daoSupport.delete("io.renren.modules.sys.dao.SysUserDao.delChatHead", pageData);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PageData selectUserDept(PageData pageData) throws Exception {
        PageData object = (PageData) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectUserDept", pageData);
        return object;
    }

    @Override
    public void saveUserDept(PageData pageData) throws Exception {
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.saveUserDept", pageData);
    }

    @Override
    public void insertUserRole(PageData pageData) throws Exception {
        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.insertUserRole", pageData);
    }

    @Override
    public void deleUserRole(PageData pageData) throws Exception {
        daoSupport.delete("io.renren.modules.sys.dao.SysUserDao.deleUserRole", pageData);
    }

    @Override
    public List<PageData> getUserPermission(PageData pageData) throws Exception {
        List<PageData> list = (List<PageData>) daoSupport
                .findForList("io.renren.modules.sys.dao.SysUserDao.getUserPermission", pageData);
        return list;
    }

    @Override
    public void updateUserInfo(PageData pageData) throws Exception {
        daoSupport.update("io.renren.modules.sys.dao.SysUserDao.updateUserInfo", pageData);
    }

    @Override
    public Long queryByUserName(PageData pageData) throws Exception {
//        PageData forObject = (PageData) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectIdByUserName", pageData);
//        return forObject.getValueOfInteger("userId");
        return (Long) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectIdByUserName", pageData);
    }

    @Override
    public PageData selectEmailAndPhoneByUserName(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectEmailAndPhoneByUserName", pageData);
    }

    @Override
    public Boolean selectUserByPersionnum(PageData pageData) throws Exception {
        return (Long) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectCountByUserName", pageData) > 0 ? true : false;
    }

    @Override
    public void add(PageData pageData) throws Exception {
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        pageData.put("salt", salt);
        String password = pageData.getValueOfString("password");
        pageData.put("password", ShiroUtils.sha256(password, salt));

        daoSupport.save("io.renren.modules.sys.dao.SysUserDao.add", pageData);
    }

    @Override
    public List<PageData> getusersByusernameAndcor(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("io.renren.modules.sys.dao.SysUserDao.getusersByusernameAndcor",pageData);
    }

    @Override
    public PageData selectUserByUsernameNotContentCor(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("io.renren.modules.sys.dao.SysUserDao.selectUserByUsernameNotContentCor",pageData);
    }

}
