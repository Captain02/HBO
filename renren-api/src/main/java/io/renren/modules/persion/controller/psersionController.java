package io.renren.modules.persion.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.persion.service.PersionTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(tags = {"用户发布"})
@RequestMapping("/sys/psersion")
public class psersionController extends BaseController {

    PersionTopicService persionTopicService;

    @ApiOperation(value = "用户发布分页信息",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R persionTopic(@ApiIgnore Page page) throws Exception {
        int currPage = page.getCurrPage();
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> data = persionTopicService.perspersionTopic(page);
        int currPage1 = page.getCurrPage();
        if (currPage != currPage1){
            return R.ok();
        }

        return R.ok().put("data",data);
    }
}
