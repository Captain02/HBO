package io.renren.modules.sms.com.chuanglan.sms.service;

import io.renren.common.entity.PageData;
import io.renren.modules.smsnum.SmsNumService;
import io.renren.modules.sys.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    SysDictService sysDictService;

    @Autowired
    SmsNumService smsNumService;


    @Override
    public PageData consumerSMS(PageData pageData) throws Exception {
        Integer num = pageData.getCastValueOfInteger("num");
        if (num == 0){
            return null;
        }
        List<PageData> data = smsNumService.select(pageData);
//        for (PageData datum : data) {
        PageData datum = data.get(0);
        Integer datanum = datum.getValueOfInteger("num");
            if (datanum >= num) {
                PageData updatepd = new PageData();
                updatepd.put("id", datum.getValueOfInteger("id"));
                updatepd.put("num", datanum - num);
                smsNumService.updatepd(updatepd);
                updatepd.put("noticeid", pageData.getValueOfLong("noticeid"));
                updatepd.put("num", num);
                smsNumService.saveSmsNotice(updatepd);
            } else {
                PageData updatepd = new PageData();
                updatepd.put("id", datum.getValueOfInteger("id"));
                updatepd.put("num", 0);
                smsNumService.updatepd(updatepd);
                updatepd.put("noticeid", pageData.getValueOfLong("noticeid"));
                updatepd.put("num", datanum);
                smsNumService.saveSmsNotice(updatepd);
                Integer temp = 0;
                temp = num - datanum;
                pageData.put("num",temp);
                consumerSMS(pageData);
            }
//        }
        return null;
    }
}
