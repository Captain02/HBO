package io.renren.modules.images.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.images.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/img")
@Api("社团相册的控制器")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;

    /**
     * 根据社团id获取社团相册
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据社团id获取社团相册", notes = "根据社团id获取社团相册", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "corid", value = "社团id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页显示记录数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "currPage", value = "当前页", required = true, dataType = "Integer"),
    })
    public R list(Page page) {
        PageData pageData = this.getPageData();
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
            @ApiImplicitParam(paramType = "query", name = "imagename", value = "图片名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "图片id", required = true, dataType = "Integer")
    })
    public R delImage(HttpServletRequest request) {
        PageData pageData = this.getPageData();
        System.out.println(pageData.toString());
        try {
            imageService.delImage(pageData, request);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 单个文件上传
     *
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "单个文件上传", notes = "单个文件上传", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "picture", value = "图片文件", required = true, dataType = "String")
    public R save(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        System.out.println("执行了单个文件上传");
        try {
            PageData pageData = this.getPageData();
            pageData = imageService.save(pageData, picture, request);
            System.out.println(pageData.toString());
            //将文件在服务器的存储路径返回
            return R.ok().put("data", pageData);
        } catch (IOException e) {
            System.out.println("上传失败");
            e.printStackTrace();
            return R.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 批量文件上传
     *
     * @return
     */
    @PostMapping("/batch")
    @ApiOperation(value = "批量文件上传", notes = "批量文件上传", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "picture", value = "多个图片文件", required = true, dataType = "String")
    public R batch(@RequestParam("picture") MultipartFile[] picture, HttpServletRequest request) {
        System.out.println("执行了多个文件上传");

        Map<String, Object> pageDataMap = new HashMap<>();
        for (int i = 0; i < picture.length; i++) {
            try {
                PageData pageData = this.getPageData();
                pageData = imageService.save(pageData, picture[i], request);
                pageDataMap.put(Integer.toString(i), pageData);
            } catch (IOException e) {
                System.out.println("上传失败");
                e.printStackTrace();
                return R.error(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(e.getMessage());
            }
        }
        System.out.println(pageDataMap);
        //将文件在服务器的存储路径返回
        return R.ok().put("data", pageDataMap);
    }
}
