package io.renren.modules.images.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public interface ImageService {
    List<PageData> getList(Page page) throws Exception;

    PageData save(PageData pageData, MultipartFile picture, HttpServletRequest request) throws Exception;

    Map<String, Object> save(PageData pageData, MultipartFile[] picture, HttpServletRequest request) throws Exception;

    void delImage(PageData pageData, HttpServletRequest request) throws Exception;
}
