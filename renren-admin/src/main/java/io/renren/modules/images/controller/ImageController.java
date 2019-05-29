package io.renren.modules.images.controller;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.images.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/img")
@Api(value = "/sys/img", tags = "社团相册模块")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private CommService commService;

    /**
     * 根据社团id获取社团相册
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据社团id获取社团相册", notes = "根据社团id获取社团相册", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "currPage", value = "当前页", required = true, dataType = "Integer"),
    })
    public R list(@ApiIgnore Page page) {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"corId");
        page.setPd(pageData);
        try {
            List<PageData> images = imageService.getList(page);
            return R.ok().put("page", page).put("date", images);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除图片
     * 参数：
     * imagename：图片名
     * id：图片id
     *
     * @param request
     * @return
     */
    @PostMapping("/del")
    @ApiOperation(value = "删除图片", notes = "删除图片", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "图片路径", required = true, dataType = "String"),
            @ApiImplicitParam(name = "id", value = "图片id", required = true, dataType = "Integer")
    })
    public R delImage(HttpServletRequest request) {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, new String[]{"url", "id"});
        System.out.println(pageData.toString());
        String url = (String) pageData.get("url");
        if (commService.deleteFile(url) && imageService.del(pageData)) {
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 单个文件上传
     *
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "单个文件上传", notes = "单个文件上传", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "picture", value = "图片文件", required = true, dataType = "File"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer")
    })
    public R save(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        System.out.println("执行了单个文件上传");
        //文件上传
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"corId");
        String path = commService.uploadFile(picture, request, "/file/image/");
        if (path == null) {
            return R.error("文件上传失败");
        }
        //保存到数据库
        pageData.put("url", path);
        pageData.put("imagename", picture.getOriginalFilename());
        if (imageService.save(pageData) == null) {
            return R.error("保存图片失败");
        } else {
            return R.ok().put("data", pageData);
        }
    }

    /**
     * 批量文件上传
     *
     * @return
     */
    @PostMapping("/batch")
    @ApiOperation(value = "批量文件上传", notes = "批量文件上传", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "picture", value = "多个图片文件",
                    allowMultiple=true,required = true, dataType = "File"),
            @ApiImplicitParam(name = "corId", value = "社团id", required = true, dataType = "Integer")
    })
    public R batch(@RequestParam("picture") MultipartFile[] picture, HttpServletRequest request) {
        System.out.println("执行了多个文件上传");

        Map<String, Object> pageDataMap = new HashMap<>();
        //文件上传
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"corId");

        for (int i = 0; i < picture.length; i++) {
            String path = commService.uploadFile(picture[i], request, "/file/image/");
            if (path == null) {
                return R.error("文件上传失败");
            }
            //保存到数据库
            pageData.put("url", path);
            pageData.put("imagename", picture[i].getOriginalFilename());
            if (imageService.save(pageData) == null) {
                return R.error("保存图片失败");
            } else {
                pageDataMap.put(Integer.toString(i), pageData);
            }
        }
        System.out.println(pageDataMap);
        //将文件在服务器的存储路径返回
        return R.ok().put("data", pageDataMap);
    }
}
