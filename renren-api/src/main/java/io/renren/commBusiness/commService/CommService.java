package io.renren.commBusiness.commService;

import io.renren.common.entity.PageData;

import java.util.List;

public interface CommService {
    List<PageData> getselectes(PageData pageData) throws Exception;
}
