package io.renren.modules.notice;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> noticListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("NoticDao.noticlistPage", page);
    }

    @Override
    public PageData info(PageData pageData) throws Exception {
        return (PageData) daoSupport.findForObject("NoticDao.info",pageData);
    }
}
