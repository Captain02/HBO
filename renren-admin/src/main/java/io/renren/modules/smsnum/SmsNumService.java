package io.renren.modules.smsnum;

import io.renren.common.entity.PageData;

import java.util.List;

public interface SmsNumService {
    List<PageData> select(PageData pageData) throws Exception;

    void updatepd(PageData updatepd) throws Exception;

    void saveSmsNotice(PageData updatepd) throws Exception;
}
