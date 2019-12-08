package com.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  参数提取，操作类
 */
public class ParamUtil {

    // 全局map
    static Map<String, String> map = new HashMap<>();

    /**
     *  全局map添加参数
     */
    public static void addMap(String key, String value){
        map.put(key, value);
    }

    /**
     *  反射添加对象到全局map
     */
    public void addFromObject(Object object){
        Class classObject = object.getClass();
        Field[] fields = classObject.getDeclaredFields();
        for (Field field: fields){
            String key = field.getName();
            try {
                String value = BeanUtils.getProperty(object, field.getName());
                map.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  字符串替换工具
     */
    public static String replace(String str){
        String regex = "\\$\\{(.+?)\\}";
        Pattern pattern = Pattern.compile(regex);
        // 字符串先判空再处理
        if (!StringUtils.isEmpty(str)){
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()){
                String toReplace = matcher.group();
                String toReplaceKey = matcher.group(1);
                // 替换内容
                str = str.replace(toReplace, getValueFromMap(toReplaceKey));
            }
        }
        System.out.println(str);
        return str;
    }

    /**
     *  替换参数空值处理
     */
    public static String getValueFromMap(String key){
        String s = map.get(key);
        if (s==null){
            return "";
        }
        return s;
    }


}
