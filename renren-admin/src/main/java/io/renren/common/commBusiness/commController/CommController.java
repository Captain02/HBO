package io.renren.common.commBusiness.commController;

import io.renren.common.commBusiness.commService.CommService;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController("sys/comm")
public class CommController extends BaseController {

    @Autowired
    CommService commService;

    @GetMapping("/getselectes/{colume1}/{table}")
    public R getselectes(@PathVariable("colume1") String colume1, @PathVariable("table") String table) throws Exception {
        PageData pageData = new PageData();
        pageData.put("colume1", colume1);
        pageData.put("table", table);
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
    public R uploadFile(@RequestParam("picture") MultipartFile picture, HttpServletRequest request) {
        PageData pageData = this.getPageData();
        String path = (String) pageData.get("path");
        path = commService.uploadFile(picture, request, path);
        if (path != null) {
            return R.ok().put("data", path);
        }else {
            return R.error("文件上传失败");
        }
    }

}
