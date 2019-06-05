package io.renren.modules.file.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import io.renren.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    DaoSupport daoSupport;

    @Override
    public void deleteFile(PageData pageData) throws Exception {
        daoSupport.delete("FileDao.deleteById",pageData);
    }
}
