package io.renren.modules.resume.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/resume")
public class ResumeController extends BaseController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 简历管理：
     * name 姓名
     * persionNum 学号
     * status 状态
     * currPage 当前页
     * pageSize 每页显示记录数
     *
     * @return
     */
    @PostMapping("/manage")
    public R manage(Page page) {
        PageData pageData = this.getPageData();
        page.setPd(pageData);
        CheckParameterUtil.checkParameterMap(pageData,"corId");
        if(pageData.get("status").toString()==null){
            pageData.put("status",5);
        }
        try {
            List<PageData> resume = resumeService.manage(page);

//            for(int i=0; i<5; i++){
//                pageData.put(i,i+1);
//            }
            return R.ok().put("page", page).put("date", resume);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 查看单个信息
     * @param resumeId
     * @return
     */
    @GetMapping("/{resumeId}")
    public R info(@PathVariable("resumeId") Long resumeId) {
        PageData pageData = this.getPageData();
        pageData.put("resumeId", resumeId);
        try {
            List<PageData> resume = resumeService.info(pageData);
            return R.ok().put("date", resume);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
