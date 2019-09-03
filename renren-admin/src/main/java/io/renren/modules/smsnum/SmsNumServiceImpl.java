package io.renren.modules.smsnum;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsNumServiceImpl implements SmsNumService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> select(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("SmsDao.select",pageData);
    }

    @Override
    public void updatepd(PageData updatepd) throws Exception {
        daoSupport.update("SmsDao.updatepd",updatepd);
    }

    @Override
    public void saveSmsNotice(PageData updatepd) throws Exception {
        daoSupport.save("SmsDao.saveSmsNotice",updatepd);
    }
}
