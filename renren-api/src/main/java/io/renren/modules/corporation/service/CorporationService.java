package io.renren.modules.corporation.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CorporationService {
    /**
     * 社团分页查询
     * @param page
     * @return
     * @throws Exception
     */
    List<PageData> getListPage(Page page)throws Exception;

    /**
     * 获取社团详情
     * @param id
     * @return
     * @throws Exception
     */
    Map getCorporation(Long id) throws Exception ;

    List<PageData> byIdImages(long id) throws  Exception;
}
