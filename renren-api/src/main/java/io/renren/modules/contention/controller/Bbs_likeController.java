package io.renren.modules.contention.controller;

import io.renren.annotation.Login;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.JWTUtil;
import io.renren.common.utils.R;
import io.renren.modules.contention.service.Bbs_likeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bbs_like")
@Api(tags = {"点赞"})
public class Bbs_likeController extends BaseController{
    @Autowired
    Bbs_likeService bbs_likeService;

    /**
     *点赞
     * @return
     */
    @Login
    @PostMapping("addTopic")
    @ApiOperation(value ="点赞功能", httpMethod = "POST",tags = {"点赞"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicid", value = "主题id", paramType = "query",required = true, dataType = "Integer"),
    })
    public R userInfo(HttpServletRequest req){
        PageData pageData = this.getPageData();
       String tokenReq = req.getHeader("Authorization");
       String username = JWTUtil.getUsername(tokenReq);
       try {
           Object userId =bbs_likeService.selectId(username);
           pageData.put("userId", Long.valueOf(String.valueOf(userId)));
           PageData bbs_like=bbs_likeService.save(pageData);
           if (bbs_like == null) {
               return R.error("保存失败");
           } else {
               return R.ok().put("data","点赞成功");
           }
       }catch (Exception e) {
           e.printStackTrace();
           return R.error("点赞功能"+e.getMessage());
       }
    }
}
