package cn.wenjig.crm.util;

import com.alibaba.fastjson.JSON;

/**
 * json转换的工具类
 */
public class JsonUtil {

    public static Object analysis(Object data, Class<?> clazz) {
        if (data != null) {
            return JSON.parseObject(String.valueOf(data), clazz);
        } else {
            return null;
        }
    }

    public static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

}
