package io.renren.modules.news.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
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

import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping("/sys/news")
@Api(value = "/sys/news", tags = "新闻模块")
public class NewsController extends BaseController {

    @Autowired
    private NewsService newsService;

    /**
     * 新闻列表
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新闻分页信息", notes = "新闻分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "corid", value = "社团id", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "title", value = "新闻主题", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isNews", value = "是否有效", required = false, dataType = "Integer")
    })
    @GetMapping("/list")
    public R list(Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = newsService.newsListPage(page);
        return R.ok().put("page",page).put("data", list);
    }

    /**
     * 获取单个社团新闻详情
     * actId: 活动id
     *
     * @return
     */
    @ApiOperation(value = "新闻详情", notes = "新闻详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "新闻id", required = true, dataType = "Integer")
    })
    @GetMapping("/getNews")
    public R getNews() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "id");
        pageData = newsService.getNewsById(pageData);
        return R.ok().put("data", pageData);
    }

    /**
     * 获取所有留言
     * topicid：主题id
     * @param page
     * @return
     * @throws Exception
     */
    @GetMapping("/getReplies")
    public R getReplies(Page page) throws Exception {
        PageData pageData = this.getPageData();
        pageData.put("id", 0);
        page.setPd(pageData);
        List<PageData> replies = newsService.getReplies(page);

        return R.ok().put("data",replies).put("page",page);
    }
}
