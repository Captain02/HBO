package io.renren.modules.corporation.controller;

import io.renren.annotation.Login;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.modules.contention.service.Bbs_likeService;
import io.renren.modules.corporation.service.Cor_userService;
import io.renren.modules.corporation.service.CorporationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/corporation")
@Api("社团")
public class CorporationController extends BaseController{

    @Autowired
    CorporationService corporationService;

    @Autowired
    Bbs_likeService bbs_likeService;

    @Autowired
    Cor_userService cor_userService;
    /**
     * 获取社团的分页信息
     * @return
     */
    @GetMapping("/getListPage")
    @ApiOperation(value = "社团分页信息", notes = "社团分页信息", httpMethod = "GET")
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
            List<PageData> corporations = corporationService.getListPage(page);
            if (currPage != page.getCurrPage()) {
                return R.ok();
            }
            return R.ok().put("page", page).put("date", corporations);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 根据活动id获取活动详情
     * @return
     */
    @GetMapping("/getCorporation")
    @ApiOperation(value = "社团详情", notes = "社团详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "社团id", required = true, dataType = "Integer")
    })
    public  R getCorporation(){
        PageData pageData =this.getPageData();
        try{
            long id=pageData.getValueOfInteger("id");
            Map corporation= corporationService.getCorporation(id);
            if (corporation == null){
                return R.error("社团详情为空");
            }else{
                return R.ok().put("date",corporation);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取社团相册
     * @return
     */
    @GetMapping("/getImages")
    @ApiOperation(value = "社团相册", notes = "社团相册", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "社团id", required = true, dataType = "Integer")
    })
    public  R getImages(){
        PageData pageData =this.getPageData();
        try{
            long id=pageData.getValueOfInteger("id");
            PageData images= corporationService.byIdImages(id);
            if (images == null){
                return R.ok().put("date","");
            }else{
                return R.ok().put("date",images);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
    /**
     *社团报名
     * @return
     */
    @Login
    @GetMapping("addCor")
    @ApiOperation(value ="社团报名", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, dataType = "Integer"),
    })
    public R addAct(HttpServletRequest req) {
        PageData pageData = this.getPageData();
        String tokenReq = req.getHeader("Authorization"); //从header中获取token
        String username = JWTUtil.getUsername(tokenReq); //根据token获取username
        try {
            Object userId = bbs_likeService.selectId(username);//根据username获取userid
            pageData.put("userId", Long.valueOf(String.valueOf(userId)));
            int count = cor_userService.byUserIdAndCorId(pageData);
            if (count == 1) {
                return R.ok().put("data", "您已报名该社团,请勿重复操作");//您已报名成功
            } else if (count == 0) {
                int cor_user = cor_userService.save(pageData); //保存到社团报名表
                return R.ok().put("data", "报名成功"); //报名成功
            } else
                return R.ok().put("data","");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
