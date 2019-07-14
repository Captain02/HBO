package io.renren.modules.news.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NewsService {
    List<PageData> newsListPage(Page page) throws Exception;

    PageData getNewsById(PageData pageData) throws Exception;

    List<PageData> getReplies(Page page) throws Exception;

    void add(PageData pageData) throws Exception;

    void updateNews(PageData pageData) throws Exception;

    List<PageData> getNewsReplice(Page page) throws Exception;

    void likereplies(PageData pageData) throws Exception;

    void unlikereplies(PageData pageData) throws Exception;

    void replies(PageData pageData) throws Exception;
}
