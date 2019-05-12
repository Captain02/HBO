package io.renren.common.utils;

import java.util.Map;

class CheckParameterUtil {

    public static void checkParameterMap(Map map, String... parameters) {
        if (parameters.length != 0) {
            if (null != map && !map.isEmpty()) {
                for (String parameter : parameters) {
                    if (null == map.get(parameter) || "".equals(map.get(parameter)) || "undefined".equals(map.get(parameter))) {
                        throw new RuntimeException("参数" + parameter + "不能为空");
                    }
                }
            } else {
                throw new RuntimeException("参数不能为空");
            }
        }

    }
}
