package io.renren.modules.persion.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.persion.service.PersionTopicService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersionTopicServiceImpl implements PersionTopicService {
    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> perspersionTopic(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("persionTopic.persionTopicpageList",page);
    }
}
