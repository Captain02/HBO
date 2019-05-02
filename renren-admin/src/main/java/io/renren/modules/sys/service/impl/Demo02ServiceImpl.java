package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.Demo02Dao;
import io.renren.modules.sys.entity.Demo02Entity;
import io.renren.modules.sys.service.Demo02Service;


@Service("demo02Service")
public class Demo02ServiceImpl extends ServiceImpl<Demo02Dao, Demo02Entity> implements Demo02Service {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Demo02Entity> page = this.page(
                new Query<Demo02Entity>().getPage(params),
                new QueryWrapper<Demo02Entity>()
        );

        return new PageUtils(page);
    }

}
