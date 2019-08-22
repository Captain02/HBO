package io.renren.modules.notic.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface NoticService {
    void add(PageData pageData) throws Exception;

    List<PageData> noticListPage(Page page) throws Exception;

    List<PageData> homeListPage(Page page) throws Exception;

    PageData info(PageData pageData) throws Exception;
}
