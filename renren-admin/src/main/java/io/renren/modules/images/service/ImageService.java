package io.renren.modules.images.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    List<PageData> getList(Page page) throws Exception;

    void save(PageData pageData) throws Exception;

    void delImage(PageData pageData) throws Exception;
}
