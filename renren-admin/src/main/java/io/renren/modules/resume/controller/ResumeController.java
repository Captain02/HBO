package io.renren.modules.resume.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.dao.Excel;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateUtil;
import io.renren.common.utils.CheckParameterUtil;
import io.renren.common.utils.ExcelUtil;
import io.renren.common.utils.R;
import io.renren.modules.resume.service.ResumeService;
import io.renren.modules.sys.entity.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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
        CheckParameterUtil.checkParameterMap(pageData, "corId");
        if (!pageData.containsKey("status") || pageData.getValueOfString("status").equals("")) {
            pageData.put("status", 0);
        }
        try {
            //返回简历信息
            List<PageData> resume = resumeService.manage(page);
            //返回各自状态的条数
            Map count = new LinkedHashMap();
            for (int i = 0; i < 5; i++) {
                pageData.put("statu", i + 1);
                pageData = resumeService.selectCount(pageData);
                count.put(Integer.toString(i + 1), pageData.getValueOfString("num"));
            }
            //返回总数
            pageData = resumeService.selectCount(pageData);
            count.put("total", pageData.getValueOfString("num"));

            return R.ok().put("page", page).put("data", resume).put("count", count);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 查看单个信息
     *
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
     *
     * @return
     */
    @PostMapping("/edit")
    public R edit() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "resumeId", "status");
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
     *
     * @return
     */
    @PostMapping("/delete")
    public R delete() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "resumeId");
        pageData.put("status", 0);
        try {
            resumeService.delete(pageData);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    public R exportExcel(HttpServletRequest request, HttpServletResponse response) {
        //接收参数
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId");
        //清除buffer缓存
        response.reset();
        // 指定下载的文件名
        String fileName = DateUtil.dateToString(new Date());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //封装形参
        XSSFWorkbook xssfWorkbook = null;
        List<Excel> excel = new ArrayList<Excel>();
        Map<Integer, List<Excel>> mapExcel = new LinkedHashMap<Integer, List<Excel>>();
        //设置标题列
        excel.add(new Excel("姓名", "name", 0));
        excel.add(new Excel("性别", "gender", 0));
        excel.add(new Excel("学号", "persionnum", 0));
        excel.add(new Excel("手机", "mobile", 0));
        excel.add(new Excel("学院", "college", 0));
        excel.add(new Excel("专业", "collegemajor", 0));
        excel.add(new Excel("QQ", "QQ", 0));
        excel.add(new Excel("微信", "wechart", 0));
        excel.add(new Excel("状态", "status", 0));
        //标题列行数以及cell字体样式
        mapExcel.put(0, excel);
        //工作簿名称
        String sheetName = "用户表";
        try {
            //excel标题列以及对应model字段名
            List<PageData> pageDataList = resumeService.selectAll(pageData);
            List<User> list = new ArrayList<User>();
            for (int i = 0; i < pageDataList.size(); i++) {
                User user = new User(
                        this.isNull(pageDataList.get(i).getValueOfString("name")),
                        this.isNull(pageDataList.get(i).getValueOfString("mobile")),
                        this.getStatu(pageDataList.get(i).getValueOfInteger("status")),
                        this.isNull(pageDataList.get(i).getValueOfString("persionnum")),
                        this.isNull(pageDataList.get(i).getValueOfString("wechart")),
                        this.isNull(pageDataList.get(i).getValueOfString("QQ")),
                        this.isNull(pageDataList.get(i).getValueOfString("college")),
                        this.isNull(pageDataList.get(i).getValueOfString("collegetie")),
                        this.isNull(pageDataList.get(i).getValueOfString("gender"))
                );
                list.add(user);
            }
            //生成excel
            xssfWorkbook = ExcelUtil.createExcelFile(User.class, list, mapExcel, sheetName);
            //下载
            OutputStream output;
            output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            bufferedOutPut.flush();
            xssfWorkbook.write(bufferedOutPut);
            bufferedOutPut.close();
            return R.ok("导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出异常");
        }

    }

    /**
     * 公用方法
     *
     * @param statu
     * @return
     */
    private String getStatu(int statu) {
        if (statu == 1) {
            return "录用";
        }
        if (statu == 2) {
            return "不录用";
        }
        if (statu == 3) {
            return "面试";
        }
        if (statu == 4) {
            return "笔试";
        }
        if (statu == 5) {
            return "未处理";
        }
        return "";
    }

    /**
     * 判断是否为空
     *
     * @param value
     * @return
     */
    private String isNull(String value) {
        if (value.equals("null") || value == null) {
            return "";
        }
        return value;
    }

    /**
     * 生成报表
     *
     * @return
     */
    @PostMapping("/chart")
    public R chart() {
        PageData pageData = this.getPageData();
        CheckParameterUtil.checkParameterMap(pageData, "corId");
        try {
            if (!pageData.containsKey("statu")) {
                pageData.put("statu", 0);
            }
            //性别
            pageData.put("column", "gender");
            List<PageData> genderList = resumeService.chart(pageData);
            //学院
            pageData.put("column", "college");
            List<PageData> collegeList = resumeService.chart(pageData);
            //年级
            pageData.put("column", "persionnum");
            List<PageData> persionnumList = resumeService.chart(pageData);
            for (int i = 0; i <persionnumList.size() ; i++) {
                if(persionnumList.get(i).getValueOfString("persionnum").length()<4){
                    throw new Exception("学号长度不能小于四位");
                }
                persionnumList.get(i).put("persionnum",persionnumList.get(i).getValueOfString("persionnum").substring(0,4));
            }
            //专业
            pageData.put("column", "collegetie");
            List<PageData> collegetieList = resumeService.chart(pageData);

            return R.ok().put("genderData", genderList)
                    .put("collegeData", collegeList)
                    .put("persionnumData", persionnumList)
                    .put("collegetieData", collegetieList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
