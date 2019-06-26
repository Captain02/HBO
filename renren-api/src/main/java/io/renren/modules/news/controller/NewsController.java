package io.renren.modules.news.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.news.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/sys/news")
@Api(tags = {"新闻"})
public class NewsController extends BaseController {

    @Autowired
    NewsService newsService;

    @GetMapping("/list")
    @ApiOperation(value = "新闻分页信息",tags = {"社团"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getNews(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = newsService.newlistPage(page);

        return R.ok().put("data",list);
    }
}
