package io.renren.commBusiness.commController;

import io.renren.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "通用类")
@RestController("sys/comm")
public class CommController extends BaseController {

    @Autowired
    CommService commService;

    //
    /*{TypeId}/{parentValue}*/
    @ApiOperation(value = "根据类型id和父类id得到列表",tags = {"通用类"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "Integer",name="typeId",value = "类型id",required = false),
            @ApiImplicitParam(paramType = "query",dataType = "Integer",name="parentValue",value = "父类id",required = false)
    })
    @GetMapping("/getselectes")
    public R getselectes(@RequestParam(value = "typeId", required = false) Integer typeId,
                         @RequestParam(value = "parentValue", required = false) Integer parentValue) throws Exception {
        PageData pageData = new PageData();
        pageData.put("typeId", typeId);
        pageData.put("parentValue", parentValue);
        List<PageData> data = commService.getselectes(pageData);
        return R.ok().put("data", data);
    }

    /**
     * 上传文件
     *
     * @param picture：文件名
     * @param request：请求  corId：社团id
     * @throws Exception
     */
    @PostMapping("/uploadFile")
    @ApiOperation(value = "文件上传",tags = {"通用类"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "file",name="picture",value = "文件",required = true),
    })
    public R uploadFile(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        PageData pageData = this.getPageData();
        String path = (String) pageData.get("path");
        path = commService.uploadFile(picture, request, path);
        if (path != null) {
            return R.ok().put("data", path);
        } else {
            return R.error("文件上传失败");
        }
    }




}
