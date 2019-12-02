package com.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *  map转换工具类
 */
public class MapUtil {

    // 参数分隔符
    final static String regex0 = "&";

    final static String regex1 = ";";

    String testParam = "method=loginMobile&loginname=test1&loginpass=test1&test=您好";

    /**
     * @return 入参String转换后的map
     */
    public static Map<String, String> convertString2Map(String params, String regex){
        // 先判空，健壮性要求
        if (params!=null){
            // 先分割成k,v一组的数组
            String[] strings = params.split(regex);
            Map<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < strings.length; i++) {
                // 等号split为k,v
                String[] split = strings[i].split("=");
                map.put(split[0], split[1]);
            }
            return map;
        }
        return null;
    }

    /**
     * @param params
     * regex 默认为“&”
     * @return
     */
    public static Map<String, String> convertString2Map1(String params){
        return convertString2Map(params, regex0);
    }
}
