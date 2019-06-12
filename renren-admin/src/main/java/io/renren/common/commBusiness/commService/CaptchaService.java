package io.renren.common.commBusiness.commService;

import io.renren.common.entity.PageData;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CaptchaService {
    public PageData captcha(HttpServletResponse response) throws IOException;
}
