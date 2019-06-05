package io.renren.modules.corporation.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.corporation.service.CorporationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CorporationServiceImpl implements CorporationService {
    @Autowired
    DaoSupport daoSupport;
    @Override
    public List<PageData> getListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("CorporationDao.getlistPage", page);
    }
    @Override
    public Map getCorporation(Long id) throws Exception {
        return (Map) daoSupport.findForObject("CorporationDao.byPrimaryId",id);
    }

    @Override
    public List<PageData> byIdImages(long id) throws Exception {
        return  (List<PageData>)daoSupport.findForList("CorporationDao.byIdImages",id);
    }
}
