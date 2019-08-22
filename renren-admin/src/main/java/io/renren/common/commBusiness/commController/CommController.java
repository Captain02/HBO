package io.renren.common.commBusiness.commController;

import io.renren.common.commBusiness.commService.CommService;
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
@RequestMapping("sys/comm")
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
    @PostMapping("/upload")
    @ApiOperation(value = "通用文件上传",tags = {"通用类"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "file",name="picture",value = "类型id",required = true),
    })
    public R uploadFile(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        PageData pageData = this.getPageData();
        String path = (String) pageData.get("path");
        if (path==null||path.equals("")){
            path = "/file/QrCode/FuFile/";
        }
        path = commService.uploadFile(picture, request, path);
        if (path != null) {
            return R.ok().put("data", path);
        } else {
            return R.error("文件上传失败");
        }
    }


    /**
     * 文件名称保存到数据库
     *
     * @throws Exception
     */
    @PostMapping("/addFile2DB")
    public R addFile2DB() throws Exception {
        PageData pageData = this.getPageData();
        Integer fileId = commService.addFile2DB(pageData);
        if (fileId != null) {
            return R.ok().put("data", fileId);
        } else {
            return R.error("文件上传失败");
        }
    }

    /**
     * 上传文件，然后返回文件的id和路径
     * @param picture
     * @param request
     * @return
     */
    @PostMapping("/uploadFileAndSave2DB")
    @ApiOperation(value = "上传文件，然后返回文件的id和路径",tags = {"上传文件，然后返回文件的id和路径"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",dataType = "file",name="picture",value = "类型id",required = true),
    })
    public R uploadFileAndSave2DB(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        String path = (String) pageData.get("path");
        if (path==null||path.equals("")){
            path = "/file/QrCode/FuFile/";
        }
        path = commService.uploadFile(picture, request, path);
        if (path != null) {
            String filePath = "http://www.btzmpro.com:82"+path;
            pageData.put("filePath",filePath);
            pageData.put("fileName",picture.getOriginalFilename());
            Integer fileId = commService.addFile2DB(pageData);
            if (fileId != null) {
                return R.ok().put("path",filePath).put("fileid", fileId);
            } else {
                return R.error("文件上传失败");
            }
        } else {
            return R.error("文件上传失败");
        }
    }

}
