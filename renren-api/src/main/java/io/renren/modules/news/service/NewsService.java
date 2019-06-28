package io.renren.modules.news.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface NewsService {
    List<PageData> newlistPage(Page page) throws Exception;

    List<PageData> getNewsDetail(PageData pageData) throws Exception;

    List<PageData> getNewsReplice(Page page) throws Exception;
}
