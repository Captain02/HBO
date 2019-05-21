package io.renren.modules.contention.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.contention.service.ActenrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActenrollServiceImpl implements ActenrollService{
    @Autowired
    DaoSupport daoSupport;
    @Override
    public int save(PageData pageData) throws Exception {
        return (int)daoSupport.save("ActenrollDao.save", pageData);
    }

    @Override
    public int byUserIdAndActId(PageData pageData) throws Exception {
        return (int) daoSupport.findForObject("ActenrollDao.selectIdAndactId",pageData);
    }
}
