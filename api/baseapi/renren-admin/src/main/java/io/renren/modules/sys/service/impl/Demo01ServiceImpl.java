package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.Demo01Dao;
import io.renren.modules.sys.entity.Demo01Entity;
import io.renren.modules.sys.service.Demo01Service;


@Service("demo01Service")
public class Demo01ServiceImpl extends ServiceImpl<Demo01Dao, Demo01Entity> implements Demo01Service {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<Demo01Entity> page = this.page(
                new Query<Demo01Entity>().getPage(params),
                new QueryWrapper<Demo01Entity>()
        );

        return new PageUtils(page);
    }

}
