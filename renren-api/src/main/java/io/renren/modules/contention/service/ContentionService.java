package io.renren.modules.contention.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ContentionService {
    List<PageData> getListPage(Page page)throws Exception;
    Map getCoractivity(Long actid) throws Exception ;
}
