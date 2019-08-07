package io.renren.modules.corporation.controller;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.util.DateTool;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.QrCodeUtils;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.CorporationService;
import io.renren.modules.dict.service.DictService;
import io.renren.modules.qqcodefile.service.QqCodeFileService;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/sys/corporation")
@Api(value = "/sys/corporation", tags = "社团模块")
public class CorporationController extends BaseController {

    @Autowired
    private CorporationService corporationService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    DaoSupport daoSupport;
    @Autowired
    private QqCodeFileService qqCodeFileService;
    @Autowired
    private CommService commService;

    //文件上传路径
    @Value("${fileUploudPath}")
    public String FILEUPLOUD;
    //域名信息
    @Value("${DNSConfig}")
    public String DOMAIN_NAME;

    /**
     * 获取社团详情
     *
     * @param page
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取社团详情", notes = "获取社团详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
    })
    public R list(Page page) {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        try {
            List<PageData> corporations = corporationService.getList(page);
            return R.ok().put("page", page).put("data", corporations);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 根据社团id获取社团详情
     *
     * @param
     * @return
     */
    @GetMapping("/selectByCorId")
    @ApiOperation(value = "根据社团id获取社团详情", notes = "根据社团id获取社团详情", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "Integer")
    public R selectByCorId() {
        PageData pageData = this.getPageData();
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData, "corid");
        try {
            //获取社团详情
            List<PageData> corporation = corporationService.selectByCorId(pageData);
            return R.ok().put("data", corporation);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 添加社团
     *
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加社团", notes = "添加社团", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "corname", value = "社团名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corleading", value = "负责人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cortercher", value = "负责老师", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corworkspace", value = "工作地点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corcollege", value = "学院", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corscale", value = "社团规模", required = true, dataType = "Integer")
    })
    public R add(HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        //校验参数
        String[] parameters = {"corName", "corleading", "corTercher", "corWorkspace", "corCollege"};
        CheckParameterUtil.checkParameterMap(pageData, parameters);
        //查找负责人id
        pageData.put("corleading", sysUserService.queryByUserName(pageData));

        Integer userId = sysUserService.queryByUserName(pageData);
        if (userId == null) {
            return R.error("社团负责人未注册");
        }
        //添加社团
        try {
//            System.out.println("request.getContextPath(): "+request.getContextPath()+"/upload/QrCode/");
            pageData.put("fileName", DateTool.dateToStringYYHHDD(new Date()) + pageData.get("corName").toString() + ".jpg");
            pageData.put("filePath", "/file/QrCode/Corporation/" + pageData.getValueOfString("fileName"));
            //添加社团
            corporationService.add(pageData);
            //创建二维码
            String url = "https://" + DOMAIN_NAME + "/#/code-map?Id=" + pageData.getValueOfInteger("id") + "&type=" + Const.CORPORATION_TYPE;
            QrCodeUtils.encodeByqrCodeName(url, FILEUPLOUD + "/file/QrCode/Corporation/", pageData.get("corName").toString());
            return R.ok().put("data", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加社团失败");
        }
    }

    @PostMapping("/apply")
    @ApiOperation(value = "社团申请", notes = "社团申请", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "corname", value = "社团名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corleading", value = "负责人", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cortercher", value = "负责老师", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corworkspace", value = "工作地点", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corcollege", value = "所属学院", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "corscale", value = "社团规模", required = true, dataType = "Integer")
    })
    public R apply() throws Exception {
        PageData pageData = this.getPageData();
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData, "corname", "corleading", "cortercher", "corworkspace", "corscale", "corcollege");
//        pageData.put("username",pageData.getValueOfString("corleading"));
        //校验负责人是否注册
        Integer userId = sysUserService.queryByUserName(pageData);
        if (userId == 0) {
            return R.error("社团负责人未注册");
        } else {
            pageData.put("userId", userId);
            pageData.put("corleading", userId);
        }
        //封装参数
        pageData.put("corName", pageData.getValueOfString("corname"));
        pageData.put("corTercher", pageData.getValueOfString("cortercher"));
        pageData.put("corWorkspace", pageData.getValueOfString("corworkspace"));
        pageData.put("corScale", pageData.getValueOfInteger("corscale"));
        pageData.put("corCollege", pageData.getValueOfInteger("corcollege"));
        //社团申请
        corporationService.apply(pageData);
        return R.ok();
    }

    /**
     * 删除社团
     *
     * @return
     */
    @GetMapping("/del")
    @ApiOperation(value = "根据社团id删除社团", notes = "根据社团id删除社团", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "id", value = "社团id", required = true, dataType = "Integer")
    public R del() {
        PageData pageData = this.getPageData();
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData, "id");
        try {
            corporationService.delCor(pageData);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("删除社团失败");
        }
    }

    /**
     * 根据社团id更新社团
     *
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "根据社团id更新社团", notes = "根据社团id更新社团", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "Integer")
    public R update() {
        PageData pageData = this.getPageData();
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData, "id");
        try {
            //查找负责人id
            pageData.put("corleading", sysUserService.queryByUserName(pageData));
            //更新
            corporationService.updateCor(pageData);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("更新失败");
        }
    }

    @PostMapping("/updateCorBanner")
    @ApiOperation(value = "更新社团banner", notes = "更新社团banner", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "file", value = "社团id", required = true, dataType = "file"),
    })
    public R updateCorBanner(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        //文件上传
        CheckParameterUtil.checkParameterMap(pageData, "corid");
        String path = commService.uploadFile(file, request, "/file/corBanner/");
        if (path == null) {
            return R.error("banner上传失败");
        }
        //保存到数据库
        pageData.put("path", path);
        pageData.put("filePath", path);
        pageData.put("filename", file.getOriginalFilename());
        pageData.put("fileName", file.getOriginalFilename());
        corporationService.updatefile(pageData, file, request, "bannerId");
        return R.ok().put("filePath", path);
    }


    @PostMapping("/updateCorVideo")
    @ApiOperation(value = "更新社团Video", notes = "更新社团Video", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "file", value = "社团id", required = true, dataType = "file"),
    })
    public R updateCorVideo(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        //文件上传
        CheckParameterUtil.checkParameterMap(pageData, "corid");
        String path = commService.uploadFile(file, request, "/file/corVideo/");
        if (path == null) {
            return R.error("banner上传失败");
        }
        //保存到数据库
        pageData.put("path", path);
        pageData.put("filePath", path);
        pageData.put("filename", file.getOriginalFilename());
        pageData.put("fileName", file.getOriginalFilename());
        corporationService.updatefile(pageData, file, request, "videoId");
        return R.ok().put("filePath", path);
    }

    /**
     * 根据社团id获取qq纳新群二维码
     *
     * @return
     */
    @GetMapping("/qqCodeFileList")
    @ApiOperation(value = "根据社团id获取qq纳新群二维码", notes = "根据社团id获取qq纳新群二维码", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corId", value = "社团id", paramType = "query", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", paramType = "query", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", paramType = "query", required = true, dataType = "Integer"),
    })
    public R qqCodeFileList(@ApiIgnore Page page) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId");
        page.setPd(pageData);
        List<PageData> data = qqCodeFileService.getQQCodeList(page);
        return R.ok().put("page", page).put("data", data);

    }

    /**
     * 单个qq纳新群二维码上传
     *
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "单个qq纳新群二维码上传", notes = "单个qq纳新群二维码上传", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "qqCodeFile", value = "图片文件", required = true, dataType = "File"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer")
    })
    public R save(@RequestParam("qqCodeFile") MultipartFile qqCodeFile, HttpServletRequest request) throws Exception {
        System.out.println("单个qq纳新群二维码上传");
        //文件上传
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId");
        String path = commService.uploadFile(qqCodeFile, request, "/file/qqCodeFile/");
        if (path == null) {
            return R.error("单个qq纳新群二维码上传失败");
        }
        //保存到数据库
        pageData.put("path", path);
        pageData.put("filename", qqCodeFile.getOriginalFilename());
        qqCodeFileService.save(pageData);
        return R.ok().put("data", pageData);
    }

    /**
     * 批量qq纳新群二维码上传
     *
     * @return
     */
    @PostMapping("/batch")
    @ApiOperation(value = "批量qq纳新群二维码上传", notes = "批量qq纳新群二维码上传", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "qqCodeFile", value = "多个图片文件",
                    allowMultiple = true, required = true, dataType = "File"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer")
    })
    public R batch(@RequestParam("qqCodeFile") MultipartFile[] qqCodeFile, HttpServletRequest request) throws Exception {
        System.out.println("执行了多个qq纳新群二维码上传");

        Map<String, Object> pageDataMap = new HashMap<>();
        //文件上传
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId");

        for (int i = 0; i < qqCodeFile.length; i++) {
            String path = commService.uploadFile(qqCodeFile[i], request, "/file/qqCodeFile/");
            if (path == null) {
                return R.error("文件上传失败");
            }
            //保存到数据库
            pageData.put("path", path);
            pageData.put("filename", qqCodeFile[i].getOriginalFilename());
            qqCodeFileService.save(pageData);
            pageDataMap.put(Integer.toString(i), pageData);
        }
        System.out.println(pageDataMap);
        //将文件在服务器的存储路径返回
        return R.ok().put("data", pageDataMap);
    }

    /**
     * @param request
     * @return
     */
    @PostMapping("/delQqCodeFile")
    @ApiOperation(value = "删除纳新群二维码", notes = "删除纳新群二维码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "纳新群二维码路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "图片id", required = true, dataType = "Integer")
    })
    public R delQqCodeFile(HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, new String[]{"path", "id"});
        StringBuffer url = new StringBuffer();
        url.append("/home/docker/nginx").append((String) pageData.get("path"));
        System.out.println("delurl" + url);
        if (commService.deleteFile(url.toString()) && qqCodeFileService.del(pageData)) {
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

}
