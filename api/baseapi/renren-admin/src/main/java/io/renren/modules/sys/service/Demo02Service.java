package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.Demo02Entity;

import java.util.Map;

/**
 * 
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2019-04-28 15:52:34
 */
public interface Demo02Service extends IService<Demo02Entity> {

    PageUtils queryPage(Map<String, Object> params);
}

