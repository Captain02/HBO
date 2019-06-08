package io.renren.modules.activity.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ActivityService {
    List<PageData> activityListPage(Page page) throws Exception;

    void add(PageData pageData, MultipartFile enclosure, HttpServletRequest request) throws Exception;

    PageData getActivityById(PageData pageData) throws Exception;

    void updateAct(PageData pageData,MultipartFile enclosure,HttpServletRequest request) throws Exception;

    List<PageData> getReplies(Page page) throws Exception;

    void changeProcess(PageData pageData) throws Exception;
}
