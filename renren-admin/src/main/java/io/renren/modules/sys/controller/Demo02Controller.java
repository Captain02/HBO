package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.Demo02Entity;
import io.renren.modules.sys.service.Demo02Service;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2019-04-28 15:52:34
 */
@RestController
@RequestMapping("sys/demo02")
public class Demo02Controller {
    @Autowired
    private Demo02Service demo02Service;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:demo02:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = demo02Service.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:demo02:info")
    public R info(@PathVariable("id") Integer id){
        Demo02Entity demo02 = demo02Service.getById(id);

        return R.ok().put("demo02", demo02);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:demo02:save")
    public R save(@RequestBody Demo02Entity demo02){
        demo02Service.save(demo02);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:demo02:update")
    public R update(@RequestBody Demo02Entity demo02){
        ValidatorUtils.validateEntity(demo02);
        demo02Service.updateById(demo02);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:demo02:delete")
    public R delete(@RequestBody Integer[] ids){
        demo02Service.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
