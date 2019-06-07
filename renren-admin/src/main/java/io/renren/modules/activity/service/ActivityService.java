package io.renren.modules.activity.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface ActivityService {
    List<PageData> activityListPage(Page page) throws Exception;

    void add(PageData pageData) throws Exception;

    PageData getActivityById(PageData pageData) throws Exception;

    void updateAct(PageData pageData) throws Exception;

    List<PageData> getReplies(Page page) throws Exception;
}
