package io.renren.modules.test.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.PageData;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("test")
public class TestController {
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @RequestMapping(value = "test",method = RequestMethod.GET)
    public void test() throws Exception {
        System.out.println("+69++++++++++++"+sqlSessionTemplate);
        List<PageData> object = (List<PageData>) dao.findForList("Demo01Dao.testDao", null);
        System.out.println("----------------------------------"+object);

    }

}
