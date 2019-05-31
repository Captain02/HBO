package io.renren.modules.wechart;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.entity.PageData;
import io.renren.common.utils.R;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.JWTFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@RestController
public class WechartController {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    @Autowired
    SysUserService sysUserService;

    @Value("${pcConfig}")
    String pcConfig;

    @GetMapping("/wechart/OAuth")
    public String WeChatOAuth(HttpServletRequest request) throws Exception {
        System.out.println("WeChatOAuth............");
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String redirectUrl = acquireOpenIDUrlWithCode(code);
        String responseText = HttpClientUtil.doGet(redirectUrl);

        JSONObject wechatEntity = JSONObject.parseObject(responseText);
        String openid = wechatEntity.getString("openid");

        PageData data = new PageData();
        data.put("openid", openid);
        PageData usernamePD = sysUserService.selectByOpenid(data);
        Object username = usernamePD.get("username");
        String[] split = state.split(",");
        String type = split[0];
        String corid = split[1];
        logger.info("type:"+type+",openid:"+openid+",corid:"+corid+",state:"+state);

        StringBuffer pcRedirectUrl = new StringBuffer();
        if (username != null) {
            PageData usercor = new PageData();
            usercor.put("userid", usernamePD.getValueOfInteger("user_id"));
            usercor.put("corid", corid);
            sysUserService.insertUserCor(usercor);
            pcRedirectUrl.append("http://").append(pcConfig).append("/#/result?")
                    .append("code=").append("0");

        } else {
            pcRedirectUrl.append("http://").append(pcConfig).append("/#/result?")
                    .append("code=").append(0).append("&openid=").append(openid)
                    .append("&corid=").append(corid).append("&type=").append(type);
            logger.info(pcRedirectUrl.toString());
            return pcRedirectUrl.toString();
        }
        pcRedirectUrl.append("http://").append(pcConfig).append("/#/result?")
                .append("code=").append(503).append("&openid=").append(openid)
                .append("&corid=").append(corid).append("&type=").append(type);
        logger.info(pcRedirectUrl.toString());
        return pcRedirectUrl.toString();
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
        System.out.println("code" + code.toString() + "state" + state.toString());
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
