package com.utils;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

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
    static String test3= "#{__md5(abc,1234)}";

    /**
     * @param func
     * @return 返回函数替换的内容
     */
    public static String replace(String func){
        Pattern compile = Pattern.compile(pattern);
        if (!StringUtils.isEmpty(func)){
            Matcher matcher = compile.matcher(func);
            while (matcher.find()){
                String toReplace = matcher.group();
                String replaceKey = matcher.group(1);
                func = func.replace(toReplace, getFunctions(replaceKey));
            }
            return func;
        }
        return func;
    }

    /**
     *  识别并返回函数处理内容
     */
    public static String getFunctions(String func){
        // 大小写忽略
        if ("__uuid".equalsIgnoreCase(func)){
            return UUID.randomUUID().toString().replace("-","");
        }else if("__time".equalsIgnoreCase(func)){
            // long型返回为字符串处理
            return "" + System.currentTimeMillis();
        }else if(func.startsWith("__md5")){
            String[] args = getArgs(func);
            String md5 = "";
            // 先判断参数
            if (args.length > 1) {
                md5 = DigestUtils.md5Hex(args[0] + args[1]);
            }
            return md5;
        }
        return func;
    }

    /**
     * @param func
     * @return 返回函数化参数数组
     */
    static String[] getArgs(String func){
        String regex = "\\((.+?)\\)";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(func);
        String[] split = null;
        while (matcher.find()){
            String group = matcher.group(1);
            split = group.split(",");
        }
        return split;

    }

    public static void main(String[] args) {
        replace(test3);
    }
}
