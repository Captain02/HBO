package io.renren.modules.corporation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.corporation.entity.CorcrowdEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CorcrowdDao extends BaseMapper<CorcrowdEntity> {
    public List<CorcrowdEntity> findcorcrowdById();
}
