package io.renren.modules.persion.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.persion.service.PersionTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersionTopicServiceImpl implements PersionTopicService {
    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> perspersionTopic(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("persionTopicdao.persionTopiclistPage",page);
    }

    @Override
    public PageData getDetailed(PageData data) throws Exception {
        return (PageData) daoSupport.findForObject("persionTopicdao.getDetailed",data);
    }

    @Override
    public List<PageData> getReplies(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("persionTopicdao.getReplieslistPage",page);
    }

    @Override
    public void replies(PageData pageData) throws Exception {
        daoSupport.save("persionTopicdao.replies",pageData);
    }

    @Override
    public void likepersionTopic(PageData pageData) throws Exception {
        daoSupport.save("persionTopicdao.likepersionTopic",pageData);
    }

    @Override
    public void unlikepersionTopic(PageData pageData) throws Exception {
        daoSupport.delete("persionTopicdao.unlikepersionTopic",pageData);
    }

    @Override
    public void likepersionTopicReplies(PageData pageData) throws Exception {
        daoSupport.save("persionTopicdao.likepersionTopicReplies",pageData);
    }

    @Override
    public void unlikepersionTopicReplies(PageData pageData) throws Exception {
        daoSupport.delete("persionTopicdao.unlikepersionTopicReplies",pageData);
    }

    @Override
    public void releaseTopic(PageData pageData) throws Exception {
        daoSupport.save("persionTopicdao.releaseTopic",pageData);
    }

    @Override
    public List<PageData> getUserCorStatus(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("persionTopicdao.getUserCorStatus",pageData);
    }
}
