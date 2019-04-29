package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.Demo01Entity;

import java.util.Map;

/**
 * 
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2019-04-28 15:52:34
 */
public interface Demo01Service extends IService<Demo01Entity> {

    PageUtils queryPage(Map<String, Object> params);
}

