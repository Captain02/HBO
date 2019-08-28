package io.renren.modules.activity.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.dao.Excel;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.util.DateUtil;
import io.renren.common.utils.*;
import io.renren.modules.activity.Entity.ActState;
import io.renren.modules.activity.service.ActivityService;
import io.renren.modules.file.service.FileService;
import io.renren.modules.sys.entity.User;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController()
@RequestMapping("/sys/activity")
@Api(value = "/sys/activity", tags = "活动模块")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private SysUserService sysUserService;
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


//    public R delAct(){
//
//        return R.ok()
//    }


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
        if (!"null".equals(crowdids) && !crowdids.trim().equals("")) {

            String[] split = crowdids.split(",");
            List<String> strings = Arrays.asList(split);
            //面向人群id
            if (strings.size() > 0) {
                pageData.put("actcrowdids", strings);
            }
        }
        page.setPd(pageData);
        List<PageData> list = activityService.activityListPage(page);
        return R.ok().put("page", page).put("data", list);
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
    public R updateAct(@RequestParam(value = "enclosure", required = false) MultipartFile enclosure, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "actId");
        pageData.put("actid",pageData.getValueOfString("actId"));
        activityService.updateAct(pageData, enclosure, request);
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
                "actEndTime", "profile", "processNodes");

        activityService.add(pageData, enclosure, request);
        //创建二维码
        String url = "http://" + DOMAIN_NAME + "/#/join-activity?Id=" + pageData.getValueOfInteger("actId") + "&type=" + Const.ACTIVITY_TYPE;
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
        CheckParameterUtil.checkParameterMap(pageData, "filePath", "id");
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

        return R.ok().put("data", replies).put("page", page);
    }

    @PostMapping("/changeProcess")
    public R changeProcess(@RequestBody String str) throws Exception {
//        Map<String,Map<String,ActState>> parse = (Map<String, Map<String, ActState>>) JsonUtils.parse(str);
//        List<ActState> list = (List<ActState>) parse.get("actState");
        JSONArray list = JsonUtils.parseStringToJSONArray(str);
        List<ActState> actStates = list.toJavaList(ActState.class);
        activityService.changeProcess(actStates);
        return R.ok().put("pageData", str);
    }

    public static <T> List<T> parseStringToArray(String json, Class<T> clazz) {
        List<T> list = JSON.parseArray(json, clazz);
        return list;
    }

    @GetMapping("/getUserByActId")
    public R getUserByActId(Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = activityService.getUserByActIdlistPage(page);
        return R.ok().put("page", page).put("data", list);
    }

    //点赞接口
    @PostMapping("/isLike")
    public R isLike() throws Exception {
        PageData pageData = this.getPageData();
        activityService.isLike(pageData);
        return R.ok();
    }

    @PostMapping("/isCollection")
    public R isCollection() throws Exception {
        PageData pageData = this.getPageData();
        activityService.isCollection(pageData);
        return R.ok();
    }

    //人员统计
    @GetMapping("/getActCharts")
    public R getActCharts() throws Exception {
        PageData pageData = this.getPageData();
        pageData.put("column", "sys_user.gender AS gender");
        pageData.put("column2", "sys_user.gender");
        List<PageData> groupGender = activityService.getActCharts(pageData);
        pageData.put("column", "sys_user.college AS college");
        pageData.put("column2", "sys_user.college");
        List<PageData> groupCollege = activityService.getActCharts(pageData);
        pageData.put("column", "sys_user.collegetie AS collegetie");
        pageData.put("column2", "sys_user.collegetie");
        List<PageData> groupcollegetie = activityService.getActCharts(pageData);
        pageData.put("column", "LEFT(sys_user.`username`,4) AS persionnum");
        pageData.put("column2", "LEFT(sys_user.`username`,4)");
        List<PageData> groupPersonNum = activityService.getActCharts(pageData);
        return R.ok().put("groupGender", groupGender)
                .put("groupCollege", groupCollege)
                .put("groupcollegetie", groupcollegetie)
                .put("groupPersonNum", groupPersonNum);
    }

    @PostMapping("/replies")
    public R replies() throws Exception {
        PageData pageData = this.getPageData();
        activityService.replies(pageData);
        return R.ok();
    }

    @PostMapping("/repliesLike")
    public R repliesLike() throws Exception {
        PageData pageData = this.getPageData();
        if (pageData.getValueOfInteger("type") == 1) {
            activityService.repliesLike(pageData);
        } else {
            activityService.delRepliesLike(pageData);
        }
        return R.ok();
    }

    @GetMapping("/exportExcel")
    public R exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //清除buffer缓存
        response.reset();
        // 指定下载的文件名
        String fileName = DateUtil.dateToString(new Date());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //封装形参
        XSSFWorkbook xssfWorkbook = null;
        List<Excel> excel = new ArrayList<Excel>();
        Map<Integer, List<Excel>> mapExcel = new LinkedHashMap<Integer, List<Excel>>();
        //设置标题列
        excel.add(new Excel("姓名", "name", 0));
        excel.add(new Excel("性别", "gender", 0));
        excel.add(new Excel("学号", "persionnum", 0));
        excel.add(new Excel("手机", "mobile", 0));
        excel.add(new Excel("学院", "college", 0));
        excel.add(new Excel("专业", "collegemajor", 0));
        excel.add(new Excel("QQ", "QQ", 0));
        excel.add(new Excel("微信", "wechart", 0));
//        excel.add(new Excel("状态", "status", 0));
        //标题列行数以及cell字体样式
        mapExcel.put(0, excel);
        //工作簿名称
        String sheetName = "报名表";
        //excel标题列以及对应model字段名
        List<User> list = new ArrayList<User>();
        PageData pageData = new PageData();
        List<PageData> pageDataList = this.activityService.getActivityUserId(pageData);
        for (int i = 0; i < pageDataList.size(); i++) {
            PageData data = sysUserService.getinfoByid(pageDataList.get(i));
            User user = new User(
                    this.isNull(data.getValueOfString("name")),
                    this.isNull(data.getValueOfString("mobile")),
                    this.isNull(data.getValueOfString("persionnum")),
                    this.isNull(data.getValueOfString("wechart")),
                    this.isNull(data.getValueOfString("QQ")),
                    this.isNull(data.getValueOfString("college")),
                    this.isNull(data.getValueOfString("collegetie")),
                    this.isNull(data.getValueOfString("gender"))
            );
            list.add(user);
        }
        //生成excel
        xssfWorkbook = ExcelUtil.createExcelFile(User.class, list, mapExcel, sheetName);
        //下载
        OutputStream output;
        output = response.getOutputStream();
        BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
        bufferedOutPut.flush();
        xssfWorkbook.write(bufferedOutPut);
        bufferedOutPut.close();
        return R.ok("导出成功");
    }

    private String isNull(String value) {
        if (value.equals("null") || value == null) {
            return "无";
        }
        return value;
    }

    /**
     * 上传活动QQ群二维码
     * @param qqCodeFile
     * @param request
     * @return
     */
    @PostMapping("/uploadqqcodeFile")
    @ApiOperation(value = "上传活动QQ群二维码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "file",name="qqCodeFile",value = "上传的文件",required = true),
            @ApiImplicitParam(paramType = "query",dataType = "Integer",name="actId",value = "活动id",required = true),
            @ApiImplicitParam(paramType = "query",dataType = "Integer",name="corId",value = "社团id",required = true)
    })
    public R uploadqqcodeFile(@RequestParam("qqCodeFile") MultipartFile qqCodeFile, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"actId","corId");
        //上传文件
        String path = (String) pageData.get("path");
        if (path==null||path.equals("")){
            path = "/file/qqCodeFile/";
        }
        String filePath = commService.uploadFile(qqCodeFile, request, path);
        if (filePath != null) {
            pageData.put("path",filePath);
            pageData.put("filename",qqCodeFile.getOriginalFilename());
            activityService.uploadqqcodeFile(pageData);
            return R.ok().put("path",filePath);
        } else {
            return R.error("文件上传失败");
        }
    }


}