package io.renren.modules.news.controller;

import io.renren.common.commBusiness.commService.CommService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/sys/news")
@Api(value = "/sys/news", tags = "新闻模块")
public class NewsController extends BaseController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private CommService commService;

    /**
     * 新闻列表
     *
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
        return R.ok().put("page", page).put("data", list);
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
     *
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

        return R.ok().put("data", replies).put("page", page);
    }

    /**
     * 添加新闻
     *
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加新闻", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "title", value = "新闻主题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "releaseuser", value = "发布人id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "content", value = "新闻简介", required = true, dataType = "String")
    })
    public R add() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId", "title", "releaseuser", "content");
        pageData.put("isNews", 1);
        newsService.add(pageData);
        return R.ok();
    }

    /**
     * 公共方法
     *
     * @param pageData
     * @param path
     * @param request
     * @return
     */
    private Boolean upload(PageData pageData, MultipartFile file, String path, HttpServletRequest request) {
        String filePath = commService.uploadFile(file, request, path);
        if (filePath == null) {
            return false;
        }
        pageData.put("filePath", filePath);
        pageData.put("fileName", file.getOriginalFilename());
        return true;
    }

    /**
     * 上传视频并保存到数据库
     *
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadNewsFile")
    public R upActBananer(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        //上传新闻视频
        upload(pageData, file, "/file/Activity/video/", request);
        //保存数据库
        Integer fileId = commService.addFile2DB(pageData);
        pageData.put("id", fileId);
        return R.ok().put("data", pageData);
    }

    /**
     * 更新新闻
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新闻更新", notes = "新闻更新", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newsId", value = "newsId", required = true, dataType = "Integer")
    })
    @PostMapping("/updateNews")
    public R updateNews() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "newsId");
        newsService.updateNews(pageData);
        return R.ok();
    }

    @ApiOperation(value = "新闻评论", tags = {"新闻"})
    @GetMapping("/getNewsReplice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicid", paramType = "query", value = "新闻id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getNewsReplice(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        int currPage = Integer.parseInt(pageData.getValueOfString("currPage"));
        pageData.put("parentid", "0");
        page.setPd(pageData);
        List<PageData> list = newsService.getNewsReplice(page);
        int aftercurrPage = page.getCurrPage();
        if (aftercurrPage != currPage) {
            return R.ok().put("data", null);
        }
        return R.ok().put("data", list);
    }

    @ApiOperation(value = "新闻评论点赞",tags = {"新闻"},notes = "{'type':状态（1：点赞，0取消点赞）,'userid':用户id,'repliesid':回复的id}")
    @PostMapping(value = "/likereplies", produces = "application/json;charset=utf-8")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1：点赞，0取消点赞）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliesid",paramType = "query", value = "评论id", required = true, dataType = "Integer"),
    })
    public R likereplies() throws Exception {
        PageData pageData = this.getPageData();
        Integer type = pageData.getValueOfInteger("type");
        if (type==1){
            newsService.likereplies(pageData);
        }else {
            newsService.unlikereplies(pageData);
        }
        return R.ok();
    }

    @ApiOperation(value = "进行评论",tags = {"新闻"},notes = "{'userid':评论人id,'parentid':父id,'topicid':主题id,'repliescontent':'回复内容','repliesuserid':被回复人id}")
    @PostMapping(value = "/replies",produces = "application/json;charset=utf-8")
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

    @PostMapping("/delete")
    public R delete() throws Exception {
        PageData pageData = this.getPageData();

        newsService.delete(pageData);
        return R.ok();
    }

    @ApiOperation(value = "新闻点赞",tags = {"新闻"},notes = "{'type':'状态（1：点赞，0取消点赞）','userid':'用户id','topicid':'主题id'}")
    @PostMapping(value = "/likeTopic")
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
}
