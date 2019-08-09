/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
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
    List<Long> queryAllMenuId(Long userId, Long corid);

    /**
     * 保存用户
     */
    void saveUser(SysUserEntity user, Long corid) throws Exception;

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

    //void removeUserByIds(List<Long> collect) throws Exception;

    PageData getinfoByid(PageData pageData) throws Exception;

    PageData selectUserByUsername(PageData username) throws Exception;

    void updataUserDept(PageData pageData) throws Exception;

    PageData save(PageData pageData) throws Exception;

    boolean delChatHead(PageData pageData);

    PageData selectUserDept(PageData pageData) throws Exception;

    void saveUserDept(PageData pageData) throws Exception;

    void insertUserRole(PageData pageData) throws Exception;

    void deleUserRole(PageData pageData) throws Exception;

    List<PageData> getUserPermission(PageData pageData) throws Exception;

    void updateUserInfo(PageData pageData) throws Exception;

    void removeUserByIds(List<Long> collect, Long corid);

    void saveUserCor(SysUserEntity user, Long corid) throws Exception;

    SysUserEntity selectUserByUsernameCorporaition(PageData pageData) throws Exception;

    PageData selectByOpenid(PageData data) throws Exception;

    void insertUserCor(PageData usercor) throws Exception;

    void QRSave(PageData pageData) throws Exception;

    List<String> queryAllPerms(PageData pageData) throws Exception;

    List<PageData> selectCorByUserCorid(PageData usercor) throws Exception;

    List<PageData> getUserCorsByUserName(PageData pageData) throws Exception;

    Long selectCountByUserName(PageData pageData) throws Exception;

    Long queryByUserName(PageData pageData) throws Exception;

    PageData selectEmailAndPhoneByUserName(PageData pageData) throws Exception;

    Boolean selectUserByPersionnum(PageData pageData) throws Exception;

    void add(PageData pageData) throws Exception;

    List<PageData> getusersByusernameAndcor(PageData pageData) throws Exception;

    PageData selectUserByUsernameNotContentCor(PageData pageData) throws Exception;
}
