package io.renren.modules.notice;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/sys/notic")
@Api(value = "/sys/notic", tags = "通知模块")
public class NoticController extends BaseController {

    @Autowired
    private NoticService noticService;

    @ApiOperation(value = "通知分页信息", notes = "活动分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "receiveUserId", value = "接收人id", required = false, dataType = "String")
    })
    @GetMapping("/list")
    public R list(Page page) throws Exception {
        int currPage = page.getCurrPage();
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = noticService.noticListPage(page);
        Integer currPage1 = Integer.parseInt(pageData.get("currPage").toString());
        if (currPage1!=currPage){
            return R.ok();
        }
        return R.ok().put("page", page).put("data", list);
    }

    @GetMapping("/info")
    @ApiOperation(value = "公告详情", notes = "公告详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "noticid", value = "公告id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "receiveUserId", value = "接收人id", required = true, dataType = "Integer"),
    })
    @ApiImplicitParam(name = "noticid", value = "公告id", required = true, dataType = "Integer")
    public R info() throws Exception {
        PageData pageData = this.getPageData();
        PageData notice = noticService.info(pageData);
        return R.ok().put("data", notice);
    }
}











