package io.renren.modules.sms.com.chuanglan.demo;

import com.alibaba.fastjson.JSON;
import io.renren.modules.sms.com.chuanglan.sms.request.SmsSendRequest;
import io.renren.modules.sms.com.chuanglan.sms.response.SmsSendResponse;
import io.renren.modules.sms.com.chuanglan.sms.util.ChuangLanSmsUtil;

import java.io.UnsupportedEncodingException;

/**
 * @author tianyh
 * @Description:��ͨ���ŷ���
 */
public class SmsSendDemo {
    public static final String charset = "utf-8";

    public static String account = "N7745465";

    public static String password = "TdC5EZivGm0cab";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";

        String testbytes2 = new String(hexstr2bytes("0xF0 0x9F 0x8C 0xB9"), "utf-8");

        String msg = "【百团争鸣】各位同学，上面发的在线文档是到现在为止团关系还没有转出的同学（后期应该还会有退回的），请相关同学抓紧将正确的接收团关系的信息填好，学校要求下周必须100%完成团建系统的交接，团建工作以后会越来越重要，请同学们务必重视。也请相关班级的班长、团支书通知到相关人，谢谢！---短信由风驰驾校赞助,联系电话17853461191" + testbytes2;

        String phone = "17853461191";

        String report = "false";

        String extend = "123";

        String uid = "abc123";


        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, phone, report, extend);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        System.out.println("before request string is: " + requestJson);

        String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

        System.out.println("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

        System.out.println("response  toString is :" + smsSingleResponse);
    }

    //eg. param: 0xF0 0x9F 0x8F 0x80
    public static byte[] hexstr2bytes(String hexstr) {
        String[] hexstrs = hexstr.split(" ");
        byte[] b = new byte[hexstrs.length];
        for (int i = 0; i < hexstrs.length; i++) {
            b[i] = hexStringToByte(hexstrs[i].substring(2))[0];
        }
        System.out.println("b" + b);
        return b;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) ("0123456789ABCDEF".indexOf(achar[pos]) << 4 | "0123456789ABCDEF".indexOf(achar[pos + 1]));
        }
        System.out.println("result" + result);
        return result;
    }
}
