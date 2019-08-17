package io.renren.modules.qqcodefile.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface QqCodeFileService {
    List<PageData> getQQCodeList(Page page) throws Exception;

    void save(PageData pageData) throws Exception;

    boolean del(PageData pageData) throws Exception;

    List<PageData> getQqCodeFileForQr(PageData pageData) throws Exception;
}
