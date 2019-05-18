package io.renren.modules.actenroll.controller;

import io.renren.annotation.Login;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateUtil;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.modules.actenroll.service.ActenrollService;
import io.renren.modules.bbs_like.service.Bbs_likeService;
import io.renren.modules.contention.service.ContentionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/actenroll")
@Api("点赞")
public class ActenrollController  extends BaseController {

    @Autowired
    ActenrollService actenrollService;
    @Autowired
    Bbs_likeService bbs_likeService;
    @Autowired
    ContentionService contentionService;
    /**
     *w参加活动功能
     * @return
     */
    @Login
    @GetMapping("addAct")
    @ApiOperation(value ="参加活动功能", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "活动id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "actprogressId", value = "活动进度Id", required = true, dataType = "Integer"),
    })
    public R addAct(HttpServletRequest req) {
        PageData pageData = this.getPageData();
        String tokenReq = req.getHeader("Authorization"); //从header中获取token
        String username = JWTUtil.getUsername(tokenReq); //根据token获取username
        try {
            Object userId = bbs_likeService.selectId(username);//根据username获取userid
            pageData.put("userId", Long.valueOf(String.valueOf(userId)));
            int count = actenrollService.byUserIdAndActId(pageData);
            if (count == 1) {
                return R.ok().put("data", "您已报名成功");//您已报名成功
            } else if (count == 0) {
                pageData.put("timestamp", DateUtil.getTime());  //报名时间
                int actenroller = actenrollService.save(pageData); //保存到活动报名表中
                if (actenroller == 0) {
                    return R.error("保存失败");
                } else {
                    return R.ok().put("data", "报名成功"); //报名成功
                }
            } else
                return R.error("count有问题");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加活动报名" + e.getMessage());
        }
    }
}
