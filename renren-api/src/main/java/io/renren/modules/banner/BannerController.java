package io.renren.modules.banner;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Banner")
public class BannerController extends BaseController {

    @Autowired
    BannerService bannerService;

    @ApiOperation(value = "获取Banner",tags = {"Banner"})
    @GetMapping("/getBanner")
    public R getBanner() throws Exception {
        PageData pageData = this.getPageData();

        List<PageData> data = bannerService.getBanner(pageData);

        return R.ok().put("data",data);
    }
}
