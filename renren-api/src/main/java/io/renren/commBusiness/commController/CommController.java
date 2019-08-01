package io.renren.commBusiness.commController;

import io.renren.comm.util.JsonUtils;
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
@RestController
@RequestMapping("/sys/comm")
public class CommController extends BaseController {

    @Autowired
    CommService commService;

    //
    /*{TypeId}/{parentValue}*/

    @ApiOperation(value = "用户信息修改", tags = {"通用类"}, notes = "" +
            "{'typeId':类型id，当类型为1并且,parentValue不填时，查询所有学院，当类型id=2并且parentValue也为实参时，查询专业" +
            "'parentValue':parentValue，学院id," +
            "}" +
            "比如:typeId:1,查询所有学院；" +
            "typeId:1,parentValue:4,查询id=4的学院下的所有专业；"
    )
    @GetMapping(value = "/getselectes", produces = "application/json;charset=utf-8")
    public R getselectes(@RequestBody String json) throws Exception {
        PageData pageData = JsonUtils.parseStringToObject(json,PageData.class);
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
