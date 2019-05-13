package io.renren.modules.contention.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.contention.service.ContentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentionServiceImpl  implements ContentionService {
    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> getListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("CoractivityDao.coractivitylistPage", page);
    }

    @Override
    public Boolean del(PageData pageData) {
        return null;
    }

    @Override
    public PageData save(PageData pageData) {
        return null;
    }
}
