package io.renren.modules.sys.service.impl;

import io.renren.modules.sys.dao.VerificationDao;
import io.renren.modules.sys.service.IsVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IsVerificationServiceImpl implements IsVerificationService {

    @Autowired
    VerificationDao verificationDao;
    @Override
    public String getIsVerification() {

        return verificationDao.getIsVerification();
    }
}
