package io.renren.modules.news.service.impl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public PageData getNewsById(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("NewsDao.getNewsById",pageData);
    }

    @Override
    public List<PageData> getReplies(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NewsDao.getReplieslistPage",page);
    }

    @Override
    public void add(PageData pageData) throws Exception {
        daoSupport.save("NewsDao.add", pageData);
    }

    @Override
    public void updateNews(PageData pageData) throws Exception {
        daoSupport.update("NewsDao.updateNews",pageData);
    }

    @Override
    public List<PageData> getNewsReplice(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NewsDao.getNewsReplicelistPage",page);
    }

    @Override
    public void likereplies(PageData pageData) throws Exception {
        daoSupport.save("NewsDao.likereplies",pageData);
    }

    @Override
    public void unlikereplies(PageData pageData) throws Exception {
        daoSupport.delete("NewsDao.unlikereplies",pageData);
    }

    @Override
    public void replies(PageData pageData) throws Exception {
        daoSupport.save("NewsDao.replies",pageData);
    }


}
