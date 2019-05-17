package io.renren.modules.resume.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> manage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ResumeDao.resumelistPage", page);
    }

    @Override
    public List<PageData> info(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("ResumeDao.info", pageData);
    }

    @Override
    public PageData selectCount(PageData pageData) throws Exception {
        return (PageData)daoSupport.findForObject("ResumeDao.selectCount", pageData);
    }
}
