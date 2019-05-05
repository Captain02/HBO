package io.renren.modules.images.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateUtil;
import io.renren.common.utils.R;
import io.renren.modules.corporation.service.impl.CorporationServiceImpl;
import io.renren.modules.images.service.ImageService;
import io.renren.modules.images.service.impl.ImageServiceImpl;
import javafx.collections.ObservableFloatArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/img")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;

    /**
     * 根据社团id获取社团相册
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(Page page){
        PageData pageData = this.getPageData();
        page.setPd(pageData);

        try {
            List<PageData> images = imageService.getList(page);
            return R.ok().put("page",page).put("date",images);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 单个文件上传
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestParam("picture") MultipartFile picture, HttpServletRequest request){
        //获取文件名和保存的文件
        String fileName = (String) this.uploadImage(picture,request).get("fileName");
        File targetFile = (File) this.uploadImage(picture,request).get("targetFile");

        //将文件保存到服务器指定位置
        try {
            picture.transferTo(targetFile);
            System.out.println("上传成功");
            //将路径、文件名保存到数据库
            PageData pageData = new PageData();
            pageData.put("url","/upload/"+fileName);
            pageData.put("imagename",fileName);
            pageData.put("createtime", DateUtil.getTime());
            imageService.save(pageData);
            //将文件在服务器的存储路径返回
            return R.ok().put("data",pageData);
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
     * @return
     */
    @PostMapping("/batch")
    public R batch(@RequestParam("picture") MultipartFile[] picture, HttpServletRequest request){

        Map<String ,Object> pageDataMap = new HashMap<>();

        for (int i=0; i<picture.length; i++){
            //获取文件名和保存的文件
            String fileName = (String) this.uploadImage(picture[i],request).get("fileName");
            File targetFile = (File) this.uploadImage(picture[i],request).get("targetFile");

            //将文件保存到服务器指定位置
            try {
                picture[i].transferTo(targetFile);
                System.out.println("上传成功");
                //将路径、文件名保存到数据库
                PageData pageData = new PageData();
                pageData.put("url","/upload/"+fileName);
                pageData.put("imagename",fileName);
                pageData.put("createtime", DateUtil.getTime());
                imageService.save(pageData);
                //存放到pageDataList中返回
                pageDataMap.put(Integer.toString(i),pageData);
            } catch (IOException e) {
                System.out.println("上传失败");
                e.printStackTrace();
                return R.error(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(e.getMessage());
            }
        }
        //将文件在服务器的存储路径返回
        return R.ok().put("data",pageDataMap);
    }

    /**
     * 共用方法
     * @param picture
     * @param request
     * @return
     */
    private Map<String ,Object> uploadImage(MultipartFile picture, HttpServletRequest request){
        //获取文件在服务器的储存位置
        String path = request.getSession().getServletContext().getRealPath("/upload");
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = picture.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(d);
        String fileName = date + name + "." + type;
        System.out.println("新文件名称：" + fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);

        Map<String ,Object> map = new HashMap<>();
        map.put("targetFile",targetFile);
        map.put("fileName",fileName);
        return map;
    }
}
