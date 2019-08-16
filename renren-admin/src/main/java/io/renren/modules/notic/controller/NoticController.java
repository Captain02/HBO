package io.renren.modules.notic.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.Const;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.QrCodeUtils;
import io.renren.common.utils.R;
import io.renren.modules.notic.service.NoticService;
import io.renren.modules.notic.service.impl.NoticServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping("/sys/notic")
@Api(value = "/sys/notic", tags = "通知模块")
public class NoticController extends BaseController {
    @Autowired
    private DaoSupport daoSupport;

    @Autowired
    private NoticService noticService;


    @PostMapping("/add")
    @ApiOperation(value = "添加通知", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "主题", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String"),
            @ApiImplicitParam(name = "publishUser", value = "发布人", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "receiveUserIds", value = "接收人", required = true, dataType = "String"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer")
    })
    public R add() throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "title", "content", "publishUser", "receiveUserIds", "corId");
        //取出接收人
        String receiveUserStr = pageData.getValueOfString("receiveUserIds");
        String[] split = receiveUserStr.split(",");
        List<String> receiveUserList = new ArrayList<>(Arrays.asList(split));
        pageData.put("receiveUserList",receiveUserList);
        noticService.add(pageData);
        return R.ok();
    }

    /**
     * 通知列表(有权限的查询)
     * corId：社团id
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "活动分页信息", notes = "活动分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "publishUser", value = "发送人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "receiveUser", value = "接收人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "title", value = "公告标题", required = false, dataType = "String"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String")
    })
    @GetMapping("/list")
    public R list(Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = noticService.noticListPage(page);
        return R.ok().put("page", page).put("data", list);
    }

    /**
     * 通知列表(没有权限的查询)
     * corId：社团id
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "活动分页信息", notes = "活动分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "receiveUser", value = "接收人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = false, dataType = "Integer")
    })
    @GetMapping("/homelist")
    public R homelist(Page page) throws Exception {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        List<PageData> list = noticService.homeListPage(page);
        return R.ok().put("page", page).put("data", list);
    }
}
