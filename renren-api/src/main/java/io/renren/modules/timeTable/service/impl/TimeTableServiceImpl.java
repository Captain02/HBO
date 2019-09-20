package io.renren.modules.timeTable.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.timeTable.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableServiceImpl implements TimeTableService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public PageData selectContent(PageData userid) throws Exception {
        PageData pageData = (PageData) daoSupport.findForObject("timeTableMapper.selectContent",userid);
        return pageData;
    }
}
