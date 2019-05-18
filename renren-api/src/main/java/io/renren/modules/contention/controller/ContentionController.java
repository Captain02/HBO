package io.renren.modules.contention.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.contention.service.ContentionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contention")
@Api("争鸣板块")
public class ContentionController  extends BaseController {

    @Autowired
    ContentionService contentionService;

    /**
     * 根据分页信息对活动信息进行分页
     * @param
     * @return R
     *
     */
    @GetMapping("/getListPage")
    @ApiOperation(value = "活动分页信息", notes = "活动分页信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
    })
    public R getListPage() {
        PageData pageData = this.getPageData();
        int currPage = Integer.parseInt(pageData.getValueOfString("currPage"));
        Page page = new Page();
        page.setPageSize(pageData.getValueOfInteger("pageSize"));
        page.setCurrPage(pageData.getValueOfInteger("currPage"));
        try {
            List<PageData> coractivitys = contentionService.getListPage(page);
            if (currPage != page.getCurrPage()) {
                return R.ok();
            }
            return R.ok().put("page", page).put("date", coractivitys);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 根据活动id获取活动详情
     * @return
     */
    @GetMapping("/getCoractivity")
    @ApiOperation(value = "活动详情", notes = "活动详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actid", value = "活动id", required = true, dataType = "Integer")
    })
    public  R getCoractivity(){
        PageData pageData =this.getPageData();
        try{
            long actid=pageData.getValueOfInteger("actid");
            Map coractivity= contentionService.getCoractivity(actid);
            if (coractivity == null){
                return R.error("活动详情为空");
            }else{
                return R.ok().put("date",coractivity);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.error("获取活动详情"+e.getMessage());
        }
    }
}
