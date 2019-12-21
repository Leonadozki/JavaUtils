package com.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  参数提取，操作类
 */
public class ParamUtil {

    // 全局map，threadLocal类实现对象线程独立，按插入顺序排序
    public static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>(){
        @Override
        protected Map<String, String> initialValue() {
            return new LinkedHashMap<>();
        }
    };

    /**
     *  全局map添加参数，threadLocal方式添加独立map
     */
    private static void addMap(String key, String value){
        threadLocal.get().put(key,value);
    }

    /**
     *  反射添加对象到全局map
     */
    public static void addFromObject(Object object){
        Class classObject = object.getClass();
        Field[] fields = classObject.getDeclaredFields();
        for (Field field: fields){
            String key = field.getName();
            try {
                String value = BeanUtils.getProperty(object, field.getName());
                addMap(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  结果关联处理
     */
    public static void addCorrelationFromJson(String respJson, String correlation){
        // 关联存在再处理
        if (JSON.isValid(respJson) && !StringUtils.isEmpty(correlation)){
            Map<String, String> correlate_map = MapUtil.convertString2Map2(correlation);
            Set<String> keys = correlate_map.keySet();
            // 循环取关联值
            for (String key: keys){
                String correlate_value = correlate_map.get(key);
                // 返回的json结果中获取对应的值
                Object readObject = JSONPath.read(respJson, correlate_value);
                // 如果提取不到值，全局获取（全局获取不到就是没有了）
                if (readObject==null){
                    correlate_value = ".." + correlate_value;
                    readObject = JSONPath.read(respJson, correlate_value);
                }
                // 如果对应值不为空
                if (readObject!=null){
                    // 判断对应值为list
                    if (readObject instanceof List){
                        List<Object> list = (List<Object>) readObject;
                        for (int i = 0; i < list.size(); i++){
                            // list结果再分组，放入参数map
//                            paramMap.put(key + "_g" + i, list.get(i).toString());
                            ParamUtil.addMap(key + "_g" + i, list.get(i).toString());
                        }
                    }else{
                        // 若不是list直接处理
                        ParamUtil.addMap(key, readObject.toString());
                    }
                }
            }
            System.out.println("global map: "+ParamUtil.threadLocal.get());
            // 初始化关联表
            correlate_map.clear();
        }
    }

    /**
     *  字符串替换工具
     */
    public static String replace(String str){
        String regex = "\\$\\{(.+?)}";
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
        return str;
    }

    /**
     *  替换参数空值处理
     */
    private static String getValueFromMap(String key){
        String s = threadLocal.get().get(key);
        if (s==null){
            return "";
        }
        return s;
    }


}
