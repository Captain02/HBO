package io.renren.modules.activity.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> activityListPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ActivityDao.activitylistPage", page);
    }
}
