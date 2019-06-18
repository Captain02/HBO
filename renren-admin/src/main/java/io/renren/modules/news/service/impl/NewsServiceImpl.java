package io.renren.modules.news.service.impl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private DaoSupport daoSupport;
    @Autowired
    private CommService commService;

    @Override
    public List<PageData> newsListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NewsDao.newslistPage", page);
    }
}
