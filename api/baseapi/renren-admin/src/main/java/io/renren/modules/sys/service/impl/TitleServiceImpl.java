package io.renren.modules.sys.service.impl;

import io.renren.modules.sys.dao.TitleDao;
import io.renren.modules.sys.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TitleServiceImpl implements TitleService {
    @Autowired
    TitleDao titleDao;

    @Override
    public String getTitleName() {
        return titleDao.getTitleName();
    }
}
