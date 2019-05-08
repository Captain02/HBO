package io.renren.common.commBusiness.commService.commServiceImpl;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommServiceImpl implements CommService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public List<PageData> getselectes(PageData pageData) throws Exception {
        return (List<PageData>) daoSupport.findForList("commMpper.selectComm",pageData);
    }
}
