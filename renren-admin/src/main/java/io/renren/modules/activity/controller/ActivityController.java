package io.renren.modules.activity.controller;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.util.DateTool;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.QrCodeUtils;
import io.renren.common.utils.R;
import io.renren.modules.activity.service.ActivityService;
import io.renren.modules.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/sys/activity")
@Api(value = "/sys/activity", tags = "活动模块")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommService commService;
    //文件上传路径
    @Value("${fileUploudPath}")
    public String FILEUPLOUD;
    //域名信息
    @Value("${DNSConfig}")
    public String DOMAIN_NAME;
    @Autowired
    FileService fileService;



    /**
     * 活动列表
     * corId：社团id
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "活动分页信息", notes = "活动分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "corid", value = "社团id", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "actName", value = "活动名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isAct", value = "是否有效", required = false, dataType = "Integer")
    })
    @GetMapping("/list")
    public R list(Page page) throws Exception {
        PageData pageData = this.getPageData();
        String crowdids = pageData.getValueOfString("crowdids");
        if (!"null".equals(crowdids)&&!crowdids.trim().equals("")){

            String[] split = crowdids.split(",");
            List<String> strings = Arrays.asList(split);
            //面向人群id
            if (strings.size()>0){
                pageData.put("actcrowdids",strings);
            }
        }
        page.setPd(pageData);
        List<PageData> list = activityService.activityListPage(page);
        return R.ok().put("data", list);
    }

    /**
     * 获取单个社团活动详情
     * actId: 活动id
     *
     * @return
     */
    @ApiOperation(value = "活动详情", notes = "活动详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actid", value = "活动id", required = true, dataType = "Integer")
    })
    @GetMapping("/getActivity")
    public R getActivity() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "actid");
        pageData = activityService.getActivityById(pageData);
        return R.ok().put("data", pageData);
    }

    /**
     * 更新活动
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "活动更新", notes = "活动更新", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "活动id", required = true, dataType = "Integer")
    })
    @PostMapping("/updateAct")
    public R updateAct(@RequestParam(value = "enclosure",required = false) MultipartFile enclosure,HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "actId");
        activityService.updateAct(pageData,enclosure,request);
        return R.ok();
    }

    /**
     * 添加活动
     *
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加活动", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actCorId", value = "社团id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "actName", value = "活动名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "actLeader", value = "活动负责人", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "actStartTime", value = "活动开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "actEndTime", value = "活动结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "croWdPeople", value = "活动面向人群", required = true, dataType = "String"),
            @ApiImplicitParam(name = "profile", value = "活动简介", required = true, dataType = "String"),
            @ApiImplicitParam(name = "processNodes", value = "进程节点(','分割)", required = true, dataType = "String")
    })
    public R add(@RequestParam(value = "enclosure", required = false) MultipartFile enclosure, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "actCorId", "actName", "actLeader", "actStartTime",
                "actEndTime", "croWdPeople", "profile", "processNodes");

        activityService.add(pageData,enclosure,request);
        //创建二维码
        String url = "http://" + DOMAIN_NAME + "/#/code-map?Id=" + pageData.getValueOfInteger("id") + "&type=" + Const.ACTIVITY_TYPE;
        QrCodeUtils.encodeByqrCodeName(url, FILEUPLOUD + "/file/QrCode/Activity/", pageData.get("actName").toString());
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

    @PostMapping("/uploudActivitiBananer")
    public R upActBananer(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        //0社团活动图片
        if (pageData.getValueOfInteger("type") == 0) {
            upload(pageData, file, "/file/Activity/images/", request);
        }
        //1社团活动视频
        if (pageData.getValueOfInteger("type") == 1) {
            upload(pageData, file, "/file/Activity/video/", request);
        }
        //1社团活动附件
        if (pageData.getValueOfInteger("type") == 2) {
            upload(pageData, file, "/file/Activity/enclosure/", request);
        }
        Integer fileId = commService.addFile2DB(pageData);
        pageData.put("id", fileId);
        return R.ok().put("data", pageData);
    }


    @PostMapping("/deleteActImage")
    public R deleteImage() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"filePath","id");
        commService.deleteFile(pageData.getValueOfString("filePath"));
        fileService.deleteFile(pageData);
        return R.ok();
    }

    @GetMapping("/getReplies")
    public R getReplies(Page page) throws Exception {
        PageData pageData = this.getPageData();
        pageData.put("repliesid", 0);
        page.setPd(pageData);
        List<PageData> replies = activityService.getReplies(page);

        return R.ok().put("data",replies).put("page",page);
    }

    @PostMapping("/changeProcess")
    public R changeProcess(){
        PageData pageData = this.getPageData();
        return R.ok();
    }

//    public R
}