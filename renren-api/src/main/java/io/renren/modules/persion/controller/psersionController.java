package io.renren.modules.persion.controller;

import io.renren.annotation.Login;
import io.renren.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.persion.service.PersionTopicService;
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

@Api(tags = {"用户发布"})
@RestController
@RequestMapping("/sys/psersion")
public class psersionController extends BaseController {

    @Autowired
    PersionTopicService persionTopicService;
    @Autowired
    CommService commService;

    @GetMapping("/persionTopic")
    @ApiOperation(value = "用户发布分页信息",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R persionTopic(@ApiIgnore Page page) throws Exception {
        int currPage = page.getCurrPage();
        PageData pageData = this.getPageData();
//        int currPage = Integer.parseInt(pageData.getValueOfString("currPage"));
        page.setPd(pageData);
        List<PageData> data = persionTopicService.perspersionTopic(page);
        int currPage1 = page.getCurrPage();
        if (currPage != currPage1){
            return R.ok();
        }

        return R.ok().put("data",data);
    }
    @GetMapping("/getDetailed")
    @ApiOperation(value = "信息详情",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicid",paramType = "query",value = "信息主键", required = true, dataType = "Integer"),
    })
    public R getDetailed() throws Exception {
        PageData data = this.getPageData();
        PageData pageData = persionTopicService.getDetailed(data);
        return R.ok().put("data",pageData);
    }

    @GetMapping("/getReplies")
    @ApiOperation(value = "信息评论",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicid",paramType = "query",value = "信息主键", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",paramType = "query",value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage",paramType = "query", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getReplies(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> data = persionTopicService.getReplies(page);
        return R.ok().put("data",data);
    }

    @PostMapping("/replies")
    @ApiOperation(value = "进行评论",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliceid",paramType = "query",value = "被回复id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "reolicecontent",paramType = "query",value = "回复内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentid",paramType = "query",value = "父评论id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "topicid",paramType = "query",value = "信息主键", required = true, dataType = "Integer"),
    })
    public R replies() throws Exception {
        PageData pageData = this.getPageData();
        persionTopicService.replies(pageData);
        return R.ok();
    }

    @PostMapping("/likepersionTopic")
    @ApiOperation(value = "信息点赞",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1点赞，0取消点赞）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户主键", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "topicid",paramType = "query",value = "信息主键", required = true, dataType = "Integer"),
    })
    public R likepersionTopic() throws Exception {
        PageData pageData = this.getPageData();
        Integer type = pageData.getValueOfInteger("type");
        if (type == 1){
            persionTopicService.likepersionTopic(pageData);
        }else {
            persionTopicService.unlikepersionTopic(pageData);
        }
        return R.ok();
    }

    @PostMapping("/likepersionTopicReplies")
    @ApiOperation(value = "评论点赞",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",paramType = "query",value = "状态（1点赞，0取消点赞）", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户主键", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "repliceid",paramType = "query",value = "信息主键", required = true, dataType = "Integer"),
    })
    public R likepersionTopicReplies() throws Exception {
        PageData pageData = this.getPageData();
        Integer type = pageData.getValueOfInteger("type");
        if (type == 1){
            persionTopicService.likepersionTopicReplies(pageData);
        }else {
            persionTopicService.unlikepersionTopicReplies(pageData);
        }
        return R.ok();
    }

    @Login
    @PostMapping("/releaseTopic")
    @ApiOperation(value = "发表贴(改接口必须登录加入token)",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid",paramType = "query",value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "content",paramType = "query",value = "内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "imageid",paramType = "query",value = "图片id", required = false, dataType = "Integer"),
    })
    public R releaseTopic() throws Exception {
        PageData pageData = this.getPageData();
        persionTopicService.releaseTopic(pageData);
        return R.ok();
    }

    @Login
    @PostMapping("/uploudpersionTopic")
    @ApiOperation(value = "上传文件(改接口必须登录加入token)",tags = {"用户发布"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",paramType = "query",value = "文件类型", required = true, dataType = "file"),
    })
    public R upActBananer(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        upload(pageData, file, "/file/persionTopic/images/", request);
        Integer fileId = commService.addFile2DB(pageData);
        pageData.put("id", fileId);
        return R.ok().put("data", pageData);
    }

    private Boolean upload(PageData pageData, MultipartFile file, String path, HttpServletRequest request) {
        String filePath = commService.uploadFile(file, request, path);
        if (filePath == null) {
            return false;
        }
        pageData.put("filePath", filePath);
        pageData.put("fileName", file.getOriginalFilename());
        return true;
    }
}
