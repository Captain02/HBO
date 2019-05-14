package io.renren.modules.bbs_like.service.impl;

import io.renren.commBusiness.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.bbs_like.service.Bbs_likeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bbs_likeServiceImpl implements Bbs_likeService {
    @Autowired
    private DaoSupport daoSupport;
    @Override
    public PageData save(PageData pageData) {
        try {
            daoSupport.save("Bbs_likeDao.save", pageData);
            return pageData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object selectId(String username) throws Exception {
        return  daoSupport.findForObject("Bbs_likeDao.selectId", username);
    }
}
