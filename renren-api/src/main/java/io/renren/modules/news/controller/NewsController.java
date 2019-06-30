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
import org.springframework.web.bind.annotation.PostMapping;
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
    @ApiOperation(value = "新闻分页信息",tags = {"新闻"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getNews(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        int currPage = Integer.parseInt(pageData.getValueOfString("currPage"));
        page.setPd(pageData);
        List<PageData> list = newsService.newlistPage(page);
        int aftercurrPage = page.getCurrPage();
        if (aftercurrPage!=currPage){
            return R.ok();
        }
        return R.ok().put("data",list);
    }

    @ApiOperation(value = "新闻详情",tags = {"新闻"})
    @GetMapping("/getNewsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",paramType = "query",value = "新闻id", required = true, dataType = "Integer"),
    })
    public R getNewsDetail() throws Exception {
        PageData pageData = this.getPageData();
        List<PageData> list = newsService.getNewsDetail(pageData);
        return R.ok().put("data",list);
    }

    @ApiOperation(value = "新闻评论",tags = {"新闻"})
    @PostMapping("/getNewsReplice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicid",paramType = "query",value = "新闻id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getNewsReplice(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        int currPage = Integer.parseInt(pageData.getValueOfString("currPage"));
        pageData.put("parentid","0");
        page.setPd(pageData);
        List<PageData> list = newsService.getNewsReplice(page);
        int aftercurrPage = page.getCurrPage();
        if (aftercurrPage!=currPage){
            return R.ok();
        }
        return R.ok().put("data",list);
    }

    @ApiOperation(value = "进行评论",tags = {"新闻"})
    @PostMapping("/replies")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid",paramType = "query",value = "评论人", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "parentid",paramType = "query",value = "父评论（第一层用0）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "topicid",paramType = "query", value = "主题id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliescontent",paramType = "query", value = "回复内容", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliesuserid",paramType = "query", value = "被回复人id", required = true, dataType = "Integer"),
    })
    public R replies() throws Exception {
        PageData pageData = this.getPageData();
        newsService.replies(pageData);
        return R.ok();
    }

    @ApiOperation(value = "新闻点赞",tags = {"新闻"})
    @PostMapping("/likeTopic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1：点赞，0取消点赞）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "topicid",paramType = "query", value = "主题id", required = true, dataType = "Integer"),
    })
    public R likeTopic() throws Exception {
        PageData pageData = this.getPageData();
        if (pageData.getValueOfInteger("type")==1){
            newsService.likeTopic(pageData);
        }else {
            newsService.unlikeTopic(pageData);
        }
        return R.ok();
    }

    @ApiOperation(value = "新闻评论点赞",tags = {"新闻"})
    @PostMapping("/likereplies")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1：点赞，0取消点赞）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliesid",paramType = "query", value = "评论id", required = true, dataType = "Integer"),
    })
    public R likereplies() throws Exception {
        PageData pageData = this.getPageData();
        if (pageData.getValueOfInteger("type")==1){
            newsService.likereplies(pageData);
        }else {
            newsService.unlikereplies(pageData);
        }
        return R.ok();
    }


}
