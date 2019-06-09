package io.renren.common.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json包，用于object转json，基于fastjson打造
 * @author yangy
 * 2018年1月23日 下午5:09:32
 * JsonUtils.java
 * @Description
 */
/**
 * @Description JSONObject代表一个json对象 {"xx":"xx","xx":"xx"}
 *              JSONArray代表多个json对象的集合[{jsonobject},{jsonobject},{jsonobject}]
 *              以下所有方法，如果转换出错时，会返回null
 */
public class JsonUtils {

	/**
	 * 将json文本转换为JSONArray或者JSONObject
	 * json文本可以以[]开始，也可以以{}开始，如果以[]开头，则转为JSONArray
	 * 
	 * @param json
	 * @return
	 */
	public static Object parse(String json) {
		try {
			Object o = JSON.parse(json);
			return o;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将string类型的json转换为JSONObject
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject parseStringToJSONObject(String json) {
		try {
			JSONObject jo = JSON.parseObject(json);
			return jo;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将string类型的json转换为JSONArray
	 * 
	 * @param json
	 */
	public static JSONArray parseStringToJSONArray(String json) {
		try {
			JSONArray ja = JSON.parseArray(json);
			return ja;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 将string类型的json转换为javabean
	 *
	 */
	public static <T> T parseStringToObject(String json, Class<T> clazz) {
		try {
			T t = JSON.parseObject(json, clazz);
			return t;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将string类型的json转换为list of javabean
	 *
	 */
	public static <T> List<T> parseStringToArray(String json, Class<T> clazz) {
		try {
			List<T> list = JSON.parseArray(json, clazz);
			return list;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 将javabean转换为string类型的json
	 *
	 */
	public static String parseObjectToString(Object object) {
		try {
			String json = JSON.toJSONString(object);
			return json;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 将map转换为JSONObject
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JSONObject parseMapToJSONObject(Map map) {
		try {
			JSONObject jo = JSONObject.parseObject(JSON.toJSONString(map));
			return jo;
		} catch (Exception e) {
			return null;
		}
	}
//	public static void main(String[] args) {
//		String json = "{\"name\":\"B\",\"age\":\"D\"},{\"name\":\"B\",\"age\":\"D\"}";
//		Map a = new HashMap();
//		a.put("name", "sadf");
//		JSONArray d = parseStringToJSONArray(json);
//		System.out.println(d);
//	}
}
