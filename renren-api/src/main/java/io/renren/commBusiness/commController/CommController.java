package io.renren.commBusiness.commController;

import io.renren.commBusiness.commService.CommService;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("sys/comm")
public class CommController {

    @Autowired
    CommService commService;

    @GetMapping("/getselectes/{colume1}/{table}")
    public R getselectes(@PathVariable("colume1")String colume1, @PathVariable("table")String table) throws Exception {
        PageData pageData = new PageData();
        pageData.put("colume1",colume1);
        pageData.put("table",table);
        List<PageData> data = commService.getselectes(pageData);
        return R.ok().put("data",data);
    }
}
