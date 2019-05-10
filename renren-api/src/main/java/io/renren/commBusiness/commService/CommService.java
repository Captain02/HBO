package io.renren.commBusiness.commService;

import io.renren.common.entity.PageData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommService {
    List<PageData> getselectes(PageData pageData) throws Exception;

//    PageData uploadFile(MultipartFile picture, HttpServletRequest request, PageData pageData) throws Exception;

    public String uploadFile(MultipartFile picture, HttpServletRequest request, String path);

    public boolean deleteFile(String filenameAndPath);
}
