package io.renren.modules.dict.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("DictService")
public class DictServiceImpl implements DictService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> selectByValue(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("DictDao.selectByValue", pageData);
    }

    @Override
    public List<PageData> selectValueById(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("DictDao.selectValueById", pageData);
    }
}
