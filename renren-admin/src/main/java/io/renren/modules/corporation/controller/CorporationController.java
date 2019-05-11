package io.renren.modules.corporation.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.CorporationService;
import io.renren.modules.corporation.service.impl.QRCodeService;
import io.renren.modules.dict.service.DictService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController()
@RequestMapping("/sys/corporation")
public class CorporationController extends BaseController {

    @Autowired
    private CorporationService corporationService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private DictService dictService;

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
//        int i = 10/0;
    }

    /**
     * 获取社团详情
     *
     * @param
     * @return
     */
    @GetMapping("/selectByCorId")
    @ApiOperation(value = "根据社团id获取社团详情", notes = "根据社团id获取社团详情", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "Integer")
    public R selectByCorId() {
        PageData pageData = this.getPageData();
        if (StringUtils.isEmpty(pageData.get("corid"))) {
            return R.error("社团id不为空");
        }
        try {
            //获取社团详情
            List<PageData> corporation = corporationService.selectByCorId(pageData);
            //根据corcollege获取所在学院
            corporation.get(0).put("id",corporation.get(0).get("corcollege"));
            corporation = dictService.selectValueById(corporation.get(0));
//            corporation.get(0).get("corcollege")=corporation.get(0).get("value");
            //根据corfaculty获取所在系别
            corporation.get(0).put("id",corporation.get(0).get("corfaculty"));
            corporation = dictService.selectValueById(corporation.get(0));
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
            @ApiImplicitParam(paramType = "query", name = "corfaculty", value = "系别", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "corscale", value = "社团规模", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "社团主页连接", required = true, dataType = "String")
    })
    public R add() {
        PageData pageData = this.getPageData();
//        if(StringUtils.isEmpty(pageData.get("url"))){
//            String url = pageData.get("url").toString();
//            return R.error("社团主页连接不能为空");
//        }
        if (StringUtils.isEmpty(pageData.get("corname"))) {
            return R.error("社团名称不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("corleading"))) {
            return R.error("负责人不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("cortercher"))) {
            return R.error("负责老师不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("corworkspace"))) {
            return R.error("工作地点不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("corcollege"))) {
            return R.error("学院不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("corfaculty"))) {
            return R.error("系别不能为空");
        }
        if (StringUtils.isEmpty(pageData.get("corscale"))) {
            return R.error("社团规模不能为空");
        }
        //添加社团
        try {
            //创建二维码
            String qrCode = qrCodeService.crateQRCode("https://www.baidu.com", 200, 200);
            pageData.put("qrCode", qrCode);
            System.out.println(qrCode);
            if (pageData.get("qrCode") != null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(pageData.get("qrCode").toString());
                pageData.put("qrCode", m.replaceAll(""));
//                StringUtils.delete(pageData.get("qrCode").toString(),"\\s*|\t|\r|\n");
            } else {
                return R.error("生成二维码失败");
            }
            //获取所属学院id
            pageData.put("value", pageData.get("corcollege").toString());
            List<PageData> pageDataList = dictService.selectByValue(pageData);
            pageData.put("corcollege", pageDataList.get(0).get("id"));
            //获取所属系
            pageData.put("value", pageData.get("corfaculty").toString());
            pageDataList = dictService.selectByValue(pageData);
            pageData.put("corfaculty", pageDataList.get(0).get("id"));
            //添加社团
            corporationService.add(pageData);
            return R.ok().put("data", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加社团失败");
        }
    }

    /**
     * 删除社团
     *
     * @return
     */
    @GetMapping("/del")
    @ApiOperation(value = "根据社团id删除社团", notes = "根据社团id删除社团", httpMethod = "GET")
    @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "Integer")
    public R del() {
        PageData pageData = this.getPageData();
        if (StringUtils.isEmpty(pageData.get("corid"))) {
            return R.error("社团id不为空");
        }
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
    public R update() {
        PageData pageData = this.getPageData();
        if (StringUtils.isEmpty(pageData.get("corid"))) {
            return R.error("社团id不为空");
        }
        try {
            //是否更新所在学院
            if (!StringUtils.isEmpty(pageData.get("corcollege"))) {
                pageData.put("value", pageData.get("corcollege").toString());
                List<PageData> pageDataList = dictService.selectByValue(pageData);
                pageData.put("corcollege", pageDataList.get(0).get("id"));
            }
            //是否更新所在系
            if (!StringUtils.isEmpty(pageData.get("corfaculty"))) {
                pageData.put("value", pageData.get("corfaculty").toString());
                List<PageData> pageDataList = dictService.selectByValue(pageData);
                pageData.put("corfaculty", pageDataList.get(0).get("id"));
            }
            //更新
            corporationService.updateCor(pageData);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("更新失败");
        }
    }

}
