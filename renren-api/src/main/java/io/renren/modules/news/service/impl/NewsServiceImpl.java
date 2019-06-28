package io.renren.modules.news.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> newlistPage(Page page) throws Exception {

        return (List<PageData>) daoSupport.findForList("news.newlistPage",page);
    }

    @Override
    public List<PageData> getNewsDetail(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("news.getNewsDetail",pageData);
    }

    @Override
    public List<PageData> getNewsReplice(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("news.getNewsReplice",page);
    }
}
