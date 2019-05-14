package io.renren.modules.bbs_like.service;

import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

@Service
public interface Bbs_likeService {
    PageData save(PageData pageData);
    Object selectId (String username) throws Exception;
}
