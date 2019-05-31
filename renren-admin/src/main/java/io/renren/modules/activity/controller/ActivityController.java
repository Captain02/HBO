package io.renren.modules.activity.controller;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateTool;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.QrCodeUtils;
import io.renren.common.utils.R;
import io.renren.modules.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController()
@RequestMapping("/sys/activity")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private CommService commService;
    //文件上传路径
    @Value("${fileUploudPath}")
    public String FILEUPLOUD;


    /**
     * 活动列表
     * corId：社团id
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/list")
    public R list(Page page) throws Exception {
        PageData pageData = this.getPageData();
//        CheckParameterUtil.checkParameterMap(pageData,"corId");
        page.setPd(pageData);
        List<PageData> list = activityService.activityListPage(page);
        System.out.println(list);
        return R.ok().put("data", list);
    }

    /**
     * 添加活动
     *
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestParam(value = "video", required = false) MultipartFile video, @RequestParam(value = "image", required = false) MultipartFile image, HttpServletRequest request) throws Exception {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "actName", "actLeader", "actStartTime", "actEndTime", "croWdPeople", "profile", "processNodes");
        pageData.put("fileName", DateTool.dateToStringYYHHDD(new Date()) + pageData.get("actName").toString() + ".jpg");
        pageData.put("filePath", "/file/QrCode/Activity/" + pageData.getValueOfString("fileName"));
        //上传宣传图
        if (!image.isEmpty()) {
            if (!this.upload(pageData, "image", image, "/file/Activity/images", request)) {
                return R.error("宣传图上传失败");
            }
        }
        //上传视频
        if (!video.isEmpty()) {
            if (!this.upload(pageData, "video", video, "/file/Activity/video", request)) {
                return R.error("视频上传失败");
            }
        }
        //插入激活状态
        pageData.put("states", 0);
        activityService.add(pageData);
        //创建二维码
        String url = "http://140.143.201.244:82/#/code-map?Id=";
        QrCodeUtils.encodeByqrCodeName(url + pageData.getValueOfInteger("id"), FILEUPLOUD + "/file/QrCode/Activity/", pageData.get("actName").toString());
        return R.ok().put("data", pageData);
    }

    /**
     * 公共方法
     *
     * @param pageData
     * @param key
     * @param path
     * @param request
     * @return
     */
    private Boolean upload(PageData pageData, String key, MultipartFile file, String path, HttpServletRequest request) {
        String filePath = commService.uploadFile(file, request, path);
        if (filePath == null) {
            return false;
        }
        pageData.put(key + "Path", filePath);
        pageData.put(key + "Name", ((MultipartFile) pageData.get(key)).getOriginalFilename());
        return true;
    }
}
