package io.renren.modules.persion.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface PersionTopicService {
    List<PageData> perspersionTopic(Page page) throws Exception;
}
