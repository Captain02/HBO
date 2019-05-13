package io.renren.modules.contention.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContentionService {
    List<PageData> getListPage(Page page)throws Exception;

    Boolean del(PageData pageData);

    PageData save(PageData pageData);
}
