package io.renren.modules.test.controller;

import io.renren.common.controller.BaseController;
import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.utils.Query;
import io.renren.modules.sys.entity.SysDictEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("test")
public class TestController extends BaseController{
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test() throws Exception {
        //new Query<SysDictEntity>().getPage(null);
        Page page = new Page();
        page.setPageSize(10);
        page.setTotalPage(0);
        page.setTotalCount(0);
        page.setCurrPage(1);
        page.setCurrentResult(0);
        page.setEntityOrField(false);
//        page.setPageStr(null);
        page.setPd(new PageData());
//        PageData pageData = new PageData();
//        pageData.put("page",page);
        System.out.println("+69++++++++++++" + sqlSessionTemplate);


        List<PageData> object = (List<PageData>) dao.findForList("Demo01Dao.selectlistPage", page);
        System.out.println("----------------------------------" + object);

    }


}
