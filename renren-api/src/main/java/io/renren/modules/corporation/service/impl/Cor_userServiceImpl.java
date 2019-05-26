package io.renren.modules.corporation.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.corporation.service.Cor_userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Cor_userServiceImpl implements Cor_userService {
    @Autowired
    DaoSupport daoSupport;

    @Override
    public int byUserIdAndCorId(PageData pageData) throws Exception {
        return (int) daoSupport.findForObject("Cor_userDao.byUserIdAndCorId",pageData);
    }

    @Override
    public int save(PageData pageData) throws Exception {
        return (int)daoSupport.save("Cor_userDao.save", pageData);
    }
}
