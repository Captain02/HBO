package io.renren.modules.dict.service;

import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;

import java.util.List;

public interface DictService {

    List<PageData> selectByValue(PageData pageData) throws Exception;
}
