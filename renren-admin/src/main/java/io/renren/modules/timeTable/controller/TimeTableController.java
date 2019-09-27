package io.renren.modules.timeTable.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.controller.BaseController;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.timeTable.service.TimeTableService;
import io.renren.modules.timeTable.utiles.ConnectJWGL;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController extends BaseController {
    @Autowired
    TimeTableService timeTableService;

    @RequestMapping("/getTimeTable")
    public R getTimeTable() throws Exception {
        PageData pageData = this.getPageData();
        PageData content = timeTableService.selectContent(pageData);
        return R.ok().put("data", content.getValueOfInteger("num"));
    }

    @ApiOperation(value = "课程信息", notes = "课程信息", httpMethod = "GET", tags = {"课表与成绩"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stuNum", paramType = "query", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", paramType = "query", value = "教务系统密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "year", paramType = "query", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "term", paramType = "query", value = "学期", required = true, dataType = "Integer"),
    })
    @GetMapping("/getTimeTableFromOfficialWeb")
    public R getTimeTableFromOfficialWeb() throws Exception {
        PageData pageData = this.getPageData();

        String stuNum = pageData.getValueOfString("stuNum");
        String password = pageData.getValueOfString("password");
        ConnectJWGL connectJWGL = new ConnectJWGL(stuNum, password);
        connectJWGL.init();
        PageData data = new PageData();
        if (connectJWGL.beginLogin()) {
            System.out.println("---查询课表---");
            System.out.print("输入学年�?2018-2019就输2018�?:");
            Integer year = pageData.getValueOfInteger("year");
            System.out.print("输入学期�?1�?2�?:");
            Integer term = pageData.getValueOfInteger("term");
            JSONArray studentTimetable = connectJWGL.getStudentTimetable(year, term);
            if (studentTimetable == null) {
                return R.error("暂无课程安排");
            } else {
                for (Iterator iterator = studentTimetable.iterator(); iterator.hasNext(); ) {
                    JSONObject lesson = (JSONObject) iterator.next();
                    data.put("xqjmc", lesson.getString("xqjmc"));
                    data.put("jc", lesson.getString("jc"));
                    data.put("kcmc", lesson.getString("kcmc"));
                    data.put("xm", lesson.getString("xm"));
                    data.put("xqmc", lesson.getString("xqmc"));
                    data.put("cdmc", lesson.getString("cdmc"));
                    data.put("zcd", lesson.getString("zcd"));
                }
            }
//            System.out.print("输入学年�?2018-2019就输2018�?:");
//            year = input.nextInt();
//            System.out.print("输入学期�?1�?2�?:");
//            term = input.nextInt();
//            connectJWGL.getStudentGrade(year,term);
            connectJWGL.logout();
        } else {
            return R.error("账号密码不正确");
        }

        return R.ok().put("data", data);
    }

    public static void main(String[] args) throws Exception {
        PageData pageData = new PageData();
        pageData.put("stuNum", "201801005021");
        pageData.put("password", "Yangguizhu1026");
        pageData.put("year", "2019");
        pageData.put("term", "1");

        String stuNum = pageData.getValueOfString("stuNum");
        String password = pageData.getValueOfString("password");
        ConnectJWGL connectJWGL = new ConnectJWGL(stuNum, password);
        connectJWGL.init();
        PageData data = new PageData();
        if (connectJWGL.beginLogin()) {
            System.out.println("---查询课表---");
            System.out.print("输入学年�?2018-2019就输2018�?:");
            Integer year = pageData.getValueOfInteger("year");
            System.out.print("输入学期�?1�?2�?:");
            Integer term = pageData.getValueOfInteger("term");
            JSONArray studentTimetable = connectJWGL.getStudentTimetable(year, term);
            if (studentTimetable == null) {
//                return R.error("暂无课程安排");
            } else {
                for (Iterator iterator = studentTimetable.iterator(); iterator.hasNext(); ) {
                    JSONObject lesson = (JSONObject) iterator.next();
                    data.put("xqjmc", lesson.getString("xqjmc"));
                    data.put("jc", lesson.getString("jc"));
                    data.put("kcmc", lesson.getString("kcmc"));
                    data.put("xm", lesson.getString("xm"));
                    data.put("xqmc", lesson.getString("xqmc"));
                    data.put("cdmc", lesson.getString("cdmc"));
                    data.put("zcd", lesson.getString("zcd"));
                }
            }
//            System.out.print("输入学年�?2018-2019就输2018�?:");
//            year = input.nextInt();
//            System.out.print("输入学期�?1�?2�?:");
//            term = input.nextInt();
//            connectJWGL.getStudentGrade(year,term);
            connectJWGL.logout();
        } else {
//            return R.error("账号密码不正确");
        }
    }

    @ApiOperation(value = "成绩信息", notes = "成绩信息", httpMethod = "GET", tags = {"课表与成绩"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stuNum", paramType = "query", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", paramType = "query", value = "教务系统密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "year", paramType = "query", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "term", paramType = "query", value = "学期", required = true, dataType = "Integer"),
    })
    @GetMapping("/getGradeFromOfficialWeb")
    public R getGradeFromOfficialWeb() throws Exception {
        PageData pageData = this.getPageData();

        String stuNum = pageData.getValueOfString("stuNum");
        String password = pageData.getValueOfString("password");
        ConnectJWGL connectJWGL = new ConnectJWGL(stuNum, password);
        connectJWGL.init();
        PageData data = new PageData();
        if (connectJWGL.beginLogin()) {
            System.out.println("---查询课表---");
            System.out.print("输入学年�?2018-2019就输2018�?:");
            Integer year = pageData.getValueOfInteger("year");
            System.out.print("输入学期�?1�?2�?:");
            Integer term = pageData.getValueOfInteger("term");
            JSONArray studentTimetable = connectJWGL.getStudentGrade(year, term);
            if (studentTimetable == null) {
                return R.error("暂无课程安排");
            } else {
                for (Iterator iterator = studentTimetable.iterator(); iterator.hasNext(); ) {
                    JSONObject lesson = (JSONObject) iterator.next();
                    data.put("kcmc", lesson.getString("kcmc"));
                    data.put("jsxm", lesson.getString("jsxm"));
                    data.put("bfzcj", lesson.getString("bfzcj"));
                    data.put("jd", lesson.getString("jd"));
                }
            }
            connectJWGL.logout();
        } else {
            return R.error("账号密码不正确");
        }

        return R.ok().put("data", data);
    }


    @ApiOperation(value = "成绩信息", notes = "成绩信息", httpMethod = "GET", tags = {"课表与成绩"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stuNum", paramType = "query", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", paramType = "query", value = "教务系统密码", required = true, dataType = "String")
    })
    @PostMapping("/loginOfficialWeb")
    public R loginOfficialWeb() throws Exception {
        PageData pageData = this.getPageData();

        String stuNum = pageData.getValueOfString("stuNum");
        String password = pageData.getValueOfString("password");
        ConnectJWGL connectJWGL = new ConnectJWGL(stuNum, password);
        connectJWGL.init();
        PageData data = new PageData();
        if (connectJWGL.beginLogin()) {
            connectJWGL.logout();
            return R.ok().put("data", "登录成功");
        } else {
            return R.error("账号密码不正确");
        }

    }
}
}
