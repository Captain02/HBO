package io.renren.modules.corporation.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CorporationService {

    Integer getCorId(PageData pageData);

    List<PageData> getList(Page page) throws Exception;

    void add(PageData pageData) throws Exception;

    void delCor(PageData pageData) throws Exception;

    void updateCor(PageData pageData) throws Exception;

    List<PageData> selectByCorId(PageData pageData) throws Exception;

    void apply(PageData pageData) throws Exception;

    void updatefile(PageData pageData, MultipartFile qqCodeFile, HttpServletRequest request) throws Exception;
}
