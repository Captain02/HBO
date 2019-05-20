package io.renren.modules.timeTable.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.timeTable.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController extends BaseController {

    @Autowired
    TimeTableService timeTableService;

    @RequestMapping("/getTimeTable")
    public R getTimeTable() throws Exception {
        PageData pageData = this.getPageData();
        PageData content = timeTableService.selectContent(pageData);
        return R.ok().put("data",content.getValueOfInteger("num"));
    }
}
