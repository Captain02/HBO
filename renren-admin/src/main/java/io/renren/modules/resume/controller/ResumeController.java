package io.renren.modules.resume.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.R;
import io.renren.modules.resume.service.ResumeService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(!pageData.containsKey("status")||pageData.get("status").toString()==null){
            pageData.put("status",5);
        }
        try {
            //返回简历信息
            List<PageData> resume = resumeService.manage(page);
            if(resume.isEmpty()){
                return R.ok().put("page", page).put("date", resume);
            }
            //返回各自状态的条数
            PageData count = new PageData();
            for(int i=0; i<5; i++){
                pageData.put("statu",i+1);
                pageData = resumeService.selectCount(pageData);
                count.put(Integer.toString(i+1),pageData.getValueOfString("num"));
            }
            //返回总数
            pageData.remove("statu");
            pageData = resumeService.selectCount(pageData);
            count.put("total",pageData.getValueOfString("num"));
            return R.ok().put("page", page).put("data", resume).put("count",count);
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
            return R.ok().put("data", resume);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 修改信息
     * @return
     */
    @PostMapping("/edit")
    public R edit() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"resumeId","statu");
        try {
            resumeService.update(pageData);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 删除信息
     * @return
     */
    @PostMapping("/delete")
    public R delete() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData,"resumeId");
        pageData.put("statu",0);
        try {
            resumeService.delete(pageData);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

}
