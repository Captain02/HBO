package io.renren.modules.images.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.modules.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> getList(Page page) throws Exception {
        return (List<PageData>)daoSupport.findForList("ImageDao.selectImageList",page);
    }

    @Override
    public void save(PageData pageData) throws Exception {
        daoSupport.save("ImageDao.save",pageData);
    }

}
