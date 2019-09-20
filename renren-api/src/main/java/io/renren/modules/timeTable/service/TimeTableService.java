package io.renren.modules.timeTable.service;


import io.renren.common.entity.PageData;

public interface TimeTableService {
    PageData selectContent(PageData pageData) throws Exception;
}
