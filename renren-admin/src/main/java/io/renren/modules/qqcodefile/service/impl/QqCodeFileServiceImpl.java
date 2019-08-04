package io.renren.modules.qqcodefile.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.qqcodefile.service.QqCodeFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("QqCodeFileService")
public class QqCodeFileServiceImpl implements QqCodeFileService {

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> getQQCodeList(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("QqCodeFileDao.qqCodeFilelistPage", page);
    }

    @Override
    public void save(PageData pageData) throws Exception {
        daoSupport.save("QqCodeFileDao.save", pageData);
    }

    @Override
    public boolean del(PageData pageData) throws Exception {
        //删除数据库中的字段
        daoSupport.delete("QqCodeFileDao.delete", pageData);
        return true;
    }
}
