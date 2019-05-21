package io.renren.modules.contention.service;

import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

@Service
public interface ActenrollService {
    int save(PageData pageData)throws Exception ;

    /**
     * 查询用户是否已参加该活动
     */
    int byUserIdAndActId(PageData pageData)throws Exception ;
}
