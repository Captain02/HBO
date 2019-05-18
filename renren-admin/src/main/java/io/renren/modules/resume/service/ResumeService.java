package io.renren.modules.resume.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface ResumeService {
    List<PageData> manage(Page page) throws Exception;

    List<PageData> info(PageData pageData) throws Exception;

    PageData selectCount(PageData pageData) throws Exception;

    void update(PageData pageData) throws Exception;

    void delete(PageData pageData) throws Exception;
}
