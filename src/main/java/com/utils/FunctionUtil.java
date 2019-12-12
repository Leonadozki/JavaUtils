package com.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  函数处理工具类
 */
public class FunctionUtil {

    // 函数格式: #{函数名}
    static String pattern = "\\#\\{(.+?)\\}";
    static String test1 = "#{__time}";
    static String test2 = "#{__UUID}";

    /**
     * @param func
     * @return 返回函数替换的内容
     */
    public static String replace(String func){
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(func);
        while (matcher.find()){
            String toReplace = matcher.group();
            String replaceKey = matcher.group(1);
            System.out.println(toReplace + " + " + replaceKey);
            func = func.replace(toReplace, getFunction(replaceKey));
        }
        System.out.println(func);
        return func;
    }

    /**
     *  识别并返回函数处理内容
     */
    public static String getFunction(String func){
        // 大小写忽略
        if ("__uuid".equalsIgnoreCase(func)){
            return UUID.randomUUID().toString().replace("-","");
        }else if("__time".equalsIgnoreCase(func)){
            // long型返回为字符串处理
            return "" + System.currentTimeMillis();
        }
        return func;
    }

    public static void main(String[] args) {
        replace(test1);
    }
}
