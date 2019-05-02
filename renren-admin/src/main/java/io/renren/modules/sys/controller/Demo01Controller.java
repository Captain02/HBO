package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.sys.entity.Demo01Entity;
import io.renren.modules.sys.service.Demo01Service;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.servlet.ModelAndView;


/**
 * 
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2019-04-28 15:52:34
 */
@RestController
@RequestMapping("sys/demo01")
public class Demo01Controller {
    @Autowired
    private Demo01Service demo01Service;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:demo01:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = demo01Service.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:demo01:info")
    public R info(@PathVariable("id") Integer id){
        Demo01Entity demo01 = demo01Service.getById(id);

        return R.ok().put("demo01", demo01);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:demo01:save")
    public R save(@RequestBody Demo01Entity demo01){
        demo01Service.save(demo01);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:demo01:update")
    public R update(@RequestBody Demo01Entity demo01){
        ValidatorUtils.validateEntity(demo01);
        demo01Service.updateById(demo01);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:demo01:delete")
    public R delete(@RequestBody Integer[] ids){
        demo01Service.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping(value = "/updatePage",method = RequestMethod.GET)
    public ModelAndView toUpdate(Integer id, Model model){
        Demo01Entity byId = demo01Service.getById(id);
        new ModelAndView("/");
        return null;
    }
}
