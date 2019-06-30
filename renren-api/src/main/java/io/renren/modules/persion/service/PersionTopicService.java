package io.renren.modules.persion.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface PersionTopicService {
    List<PageData> perspersionTopic(Page page) throws Exception;

    PageData getDetailed(PageData data) throws Exception;

    List<PageData> getReplies(Page page) throws Exception;

    void replies(PageData pageData) throws Exception;

    void likepersionTopic(PageData pageData) throws Exception;

    void unlikepersionTopic(PageData pageData) throws Exception;

    void likepersionTopicReplies(PageData pageData) throws Exception;

    void unlikepersionTopicReplies(PageData pageData) throws Exception;

    void releaseTopic(PageData pageData) throws Exception;
}
