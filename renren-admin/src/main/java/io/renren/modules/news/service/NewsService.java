package io.renren.modules.news.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface NewsService {
    List<PageData> newsListPage(Page page) throws Exception;

    PageData getNewsById(PageData pageData) throws Exception;

    List<PageData> getReplies(Page page) throws Exception;
}
