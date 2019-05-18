package io.renren.modules.corporation.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.CorporationService;
import io.renren.common.utils.QrCodeUtils;
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
        CheckParameterUtil.checkParameterMap(pageData,"corid");
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
>>>>>>> parent of 832552a... 修改bug
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
    public R add(HttpServletRequest request) {
        PageData pageData = this.getPageData();
        //校验参数
        String[] parameters = {"url","corname","corleading","cortercher","corworkspace","corcollege","corfaculty","corscale"};
        CheckParameterUtil.checkParameterMap(pageData,parameters);
        //添加社团
        try {
            //创建二维码
            String url = "https://www.baidu.com";
            String path = QrCodeUtils.encodeByqrCodeName(url,request.getSession().getServletContext().getRealPath("/HBO/upload/QrCode/"),pageData.get("corname").toString());
//            System.out.println("request.getContextPath(): "+request.getContextPath()+"/upload/QrCode/");
            System.out.println("文件存放位置："+request.getSession().getServletContext().getRealPath("/HBO/upload/QrCode/"));
            pageData.put("filePath", path);
            pageData.put("fileName", DateTool.dateToStringYYHHDD(new Date())+pageData.get("corname").toString()+".jpg");
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
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData,"corid");
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
        //校验参数
        CheckParameterUtil.checkParameterMap(pageData,"corid");
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
