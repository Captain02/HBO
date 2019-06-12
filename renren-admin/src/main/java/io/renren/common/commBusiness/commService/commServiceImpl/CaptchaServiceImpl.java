package io.renren.common.commBusiness.commService.commServiceImpl;

import com.google.code.kaptcha.Producer;
import io.renren.common.commBusiness.commService.CaptchaService;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private Producer producer;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public PageData captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(text);

        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        //将text作为值放入redis中
        String key = UUID.randomUUID().toString().replace("-", "");
        redisUtils.set(key, text, 60 * 10);
        PageData pageData = new PageData();
        pageData.put("img",encoder.encode(outputStream.toByteArray()));
        pageData.put("key",key);
        return pageData;
    }
}
