package io.renren.modules.contention.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.contention.service.RepliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepliesServiceImpl implements RepliesService {
    @Autowired
    private DaoSupport daoSupport;
    @Override
    public List<PageData> getListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("BbsRepliesDao.getReplieslistPage", page);
    }
    @Override
    public PageData save(PageData pageData) throws Exception {
       daoSupport.save("RepliesDao.save", pageData);
        return pageData;
    }
    @Override
    public PageData save_replies_like(PageData pageData) throws Exception {
        daoSupport.save("RepliesDao.save_replies_like", pageData);
        return pageData;
    }

}
