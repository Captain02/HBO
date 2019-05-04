package io.renren.modules.images.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.impl.CorporationServiceImpl;
import io.renren.modules.images.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/img")
public class ImageController extends BaseController {

    @Autowired
    private ImageServiceImpl imageService;

    @Autowired
    private CorporationServiceImpl corporationService;

    /**
     * 根据社团id获取社团相册
     * @param corName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam("corName") String corName){
        PageData pageData = new PageData();
        pageData.put("corName",corName);
        Integer corId = corporationService.getCorId(pageData);
        return R.ok();
    }
}
