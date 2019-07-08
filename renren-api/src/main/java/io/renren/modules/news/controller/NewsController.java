package io.renren.modules.news.controller;

import com.alibaba.druid.support.json.JSONUtils;
import io.renren.comm.util.JsonUtils;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

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
            return R.ok().put("data",null);
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
    @GetMapping("/getNewsReplice")
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
            return R.ok().put("data",null);
        }
        return R.ok().put("data",list);
    }

    @ApiOperation(value = "进行评论",tags = {"新闻"},notes = "{'userid':评论人id,'parentid':父id,'topicid':主题id,'repliescontent':'回复内容','repliesuserid':被回复人id}")
    @PostMapping(value = "/replies",produces = "application/json;charset=utf-8")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userid",paramType = "query",value = "评论人", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "parentid",paramType = "query",value = "父评论（第一层用0）", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "topicid",paramType = "query", value = "主题id", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "repliescontent",paramType = "query", value = "回复内容", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "repliesuserid",paramType = "query", value = "被回复人id", required = true, dataType = "Integer"),
//    })
    public R replies(@RequestBody String json) throws Exception {
        Map o = JsonUtils.parseStringToObject(json, Map.class);
        Integer userid = (Integer) o.get("userid");
        Integer parentid = (Integer) o.get("parentid");
        Integer topicid = (Integer) o.get("topicid");
        String repliescontent = (String) o.get("repliescontent");
        Integer repliesuserid = (Integer) o.get("repliesuserid");
        PageData pageData = this.getPageData();
        pageData.put("userid",userid);
        pageData.put("parentid",parentid);
        pageData.put("topicid",topicid);
        pageData.put("repliescontent",repliescontent);
        pageData.put("repliesuserid",repliesuserid);
        newsService.replies(pageData);
        return R.ok();
    }

    @ApiOperation(value = "新闻点赞",tags = {"新闻"},notes = "{'type':'状态（1：点赞，0取消点赞）','userid':'用户id','topicid':'主题id'}")
    @PostMapping(value = "/likeTopic", produces = "application/json;charset=utf-8")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1：点赞，0取消点赞）", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "topicid",paramType = "query", value = "主题id", required = true, dataType = "Integer"),
//    })
    public R likeTopic(@RequestBody String json) throws Exception {
        Map o = JsonUtils.parseStringToObject(json, Map.class);
//        Stringo.get("type")
        PageData pageData = this.getPageData();
        pageData.put("type",(Integer)(o.get("type")));
        pageData.put("userid",(Integer)(o.get("userid")));
        pageData.put("topicid",(Integer)(o.get("topicid")));

        if (pageData.getValueOfInteger("type")==1){
            newsService.likeTopic(pageData);
        }else {
            newsService.unlikeTopic(pageData);
        }
        return R.ok();
    }

    @ApiOperation(value = "新闻评论点赞",tags = {"新闻"},notes = "{'type':状态（1：点赞，0取消点赞）,'userid':用户id,'repliesid':回复的id}")
    @PostMapping(value = "/likereplies", produces = "application/json;charset=utf-8")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1：点赞，0取消点赞）", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
//            @ApiImplicitParam(name = "repliesid",paramType = "query", value = "评论id", required = true, dataType = "Integer"),
//    })
    public R likereplies(@RequestBody String json) throws Exception {
        Map o = JsonUtils.parseStringToObject(json, Map.class);
        Integer userid = (Integer) o.get("userid");
        Integer type = (Integer) o.get("type");
        Integer repliesid = (Integer) o.get("repliesid");
        PageData pageData = this.getPageData();
        pageData.put("userid",userid);
        pageData.put("repliesid",repliesid);
        if (type==1){
            newsService.likereplies(pageData);
        }else {
            newsService.unlikereplies(pageData);
        }
        return R.ok();
    }


}
