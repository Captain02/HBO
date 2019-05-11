package io.renren.common.utils;

import java.util.Map;

/**
 * 说明：检查参数是否为空，减少冗余代码。
 * 创建人：陈法磊
 * @version
 */
public class checkParameterUtil {

    public static void checkParameterMap(Map map,String...parameters){
        if(parameters.length!=0){
        if(null!=map&&!map.isEmpty()){
            for (String parameter:parameters) {
                if(null==map.get(parameter)||"".equals(map.get(parameter))||"undefined".equals(map.get(parameter))){
                    throw new RuntimeException("参数"+parameter+"不能为空");
                }
            }
        }else{
            throw new RuntimeException("参数不能为空");
        }
        }

    }
}
