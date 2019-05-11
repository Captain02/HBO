package io.renren.modules.corporation.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.corporation.service.CorporationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("CorporationService")
public class CorporationServiceImpl implements CorporationService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public Integer getCorId(PageData pageData) {
        return null;
    }

    @Override
    public List<PageData> getList(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("CorporationDao.corporationlistPage", page);
    }

    @Override
    @Transactional
    public void add(PageData pageData) throws Exception {
        //插入文件表
        daoSupport.save("FileDao.add",pageData);
        //插入社团表
        daoSupport.save("CorporationDao.add", pageData);
    }

    @Override
    public void delCor(PageData pageData) throws Exception {
        daoSupport.update("CorporationDao.update", pageData);
    }

    @Override
    public void updateCor(PageData pageData) throws Exception {
        daoSupport.update("CorporationDao.updateCor",pageData);
    }
}
