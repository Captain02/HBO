package io.renren.modules.notice;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface NoticService {
    List<PageData> noticListPage(Page page) throws Exception;

    PageData info(PageData pageData) throws Exception;
}
