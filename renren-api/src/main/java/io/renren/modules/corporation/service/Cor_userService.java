package io.renren.modules.corporation.service;

import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

@Service
public interface Cor_userService {

    /**
     * 查询是否已报名该社团
     * @param pageData
     * @return
     * @throws Exception
     */
    int byUserIdAndCorId(PageData pageData) throws Exception ;

    /**
     * 添加一条社团报名信息
     * @param pageData
     * @return
     * @throws Exception
     */
    int save(PageData pageData)throws Exception;
}
