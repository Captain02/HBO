package io.renren.modules.banner.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> getBanner(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("bannerDao.getBanner",pageData);
    }
}
