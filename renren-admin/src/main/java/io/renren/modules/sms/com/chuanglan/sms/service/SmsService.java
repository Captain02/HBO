package io.renren.modules.sms.com.chuanglan.sms.service;

import io.renren.common.entity.PageData;

public interface SmsService {
    PageData consumerSMS(PageData pageData) throws Exception;
}
