package io.renren.modules.corporation.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.util.DateTool;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.CorporationService;
import io.renren.common.utils.QrCodeUtils;
import io.renren.modules.dict.service.DictService;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/sys/corporation")
public class CorporationController extends BaseController {

    @Autowired
    private CorporationService corporationService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    DaoSupport daoSupport;

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
    @ApiImplicitParam(paramType = "query", name = "corId", value = "社团id", required = true, dataType = "Integer")
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
        String[] parameters = {"corName", "corleading", "corTercher", "corWorkspace", "corCollege", "corFaculty"};
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
        if (userId == null) {
            return R.error("社团负责人未注册");
        }else {
            pageData.put("userId",userId);
            pageData.put("corleading",userId);
        }
        //封装参数
        pageData.put("corName",pageData.getValueOfString("corname"));
        pageData.put("corTercher",pageData.getValueOfString("cortercher"));
        pageData.put("corWorkspace",pageData.getValueOfString("corworkspace"));
        pageData.put("corScale",pageData.getValueOfInteger("corscale"));
        pageData.put("corCollege",pageData.getValueOfInteger("corcollege"));
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
    @ApiImplicitParam(paramType = "query", name = "id", value = "社团id", required = true, dataType = "Integer")
    public R update() {
        PageData pageData = this.getPageData();
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData, "id");
        try {
            //更新
            corporationService.updateCor(pageData);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("更新失败");
        }
    }

}
