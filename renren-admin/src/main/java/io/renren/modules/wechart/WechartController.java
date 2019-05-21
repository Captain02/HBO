package io.renren.modules.wechart;

import io.renren.common.utils.R;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@RestController
@RequestMapping("/wechart")
public class WechartController {

    @GetMapping("/wechart")
    public R getOpenid(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("CallBackAction begin at {}"+new Date().getTime());
        String openid = request.getParameter("openid");
        System.out.println("openid"+openid);
        String code = request.getParameter("code");
        System.out.println("获到的 code is : {} "+ code);
        String redirectUrl = acquireOpenIDUrlWithCode(code);
        System.out.println("redirectUrl is : {}"+redirectUrl);
        String responseText = HttpClientUtil.doGet(redirectUrl);
        System.out.println("responseText is : {}"+ redirectUrl+ responseText);

        System.out.println("=======================================");

        return R.ok().put("responseText",responseText);
    }

    private String acquireOpenIDUrlWithCode(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxba18404f8cfe70cd&"
                + "secret=028fc71aeb278ca18a9993aec7a9aa69&code="
                + code
                + "&grant_type=authorization_code");
        return sb.toString();
    }
}
