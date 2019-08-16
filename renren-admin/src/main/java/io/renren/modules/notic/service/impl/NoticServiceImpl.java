package io.renren.modules.notic.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.notic.service.NoticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticServiceImpl implements NoticService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public void add(PageData pageData) throws Exception {
        daoSupport.save("NoticDao.add", pageData);
    }

    @Override
    public List<PageData> noticListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NoticDao.noticListPage", page);
    }

    @Override
    public List<PageData> homeListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NoticDao.homeListPage", page);
    }
}
