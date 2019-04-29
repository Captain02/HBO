package io.renren.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationDao {
    String getIsVerification();
}
