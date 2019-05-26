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
import java.util.Enumeration;

@RestController
public class WechartController {

    @GetMapping("/wechart/OAuth")
    public String WeChatOAuth(HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("WeChatOAuth............");
        StringBuffer code = new StringBuffer();
        StringBuffer state = new StringBuffer();
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
//            String name = (String) pNames.nextElement();
            code.append(request.getParameter("code"));
            state.append(request.getParameter("state"));
        }
        System.out.println("code"+code.toString()+"state"+state.toString());
//        String redirectUrl = acquireOpenIDUrlWithCode(code.toString());
//        String responseText = HttpClientUtil.doGet(redirectUrl);
//        System.out.println("responseText:" + responseText);
        return "BFl3ph7g1kvJ0PUb";
    }

    @GetMapping("/wechart/OAuth/MP_verify_BFl3ph7g1kvJ0PUb.txt")
    public String config(HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("config............");
        StringBuffer code = new StringBuffer();
        StringBuffer state = new StringBuffer();
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
//            String name = (String) pNames.nextElement();
            code.append(request.getParameter("code"));
            state.append(request.getParameter("state"));
        }
        System.out.println("code"+code.toString()+"state"+state.toString());
        return "BFl3ph7g1kvJ0PUb";
    }

    @GetMapping("/wechart")
    public String getOpenid(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = (String) pNames.nextElement();
            String value = request.getParameter(name);
            // out.print(name + "=" + value);

            String log = "name =" + name + "     value =" + value;
            System.out.println(log);
        }

        String echostr = request.getParameter("echostr");


        return echostr;
    }

    private String acquireOpenIDUrlWithCode(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2a89e726a1bf0142&"
                + "secret=1a6c6a1e57898f0976cc247a13aae7c7&code="
                + code
                + "&grant_type=authorization_code");
        return sb.toString();
    }
}
