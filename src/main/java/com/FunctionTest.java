package com;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  参数化测试类
 */
public class FunctionTest {

    public static String getFunctionValue(){
        String pattern = "\\((.+?)\\)";
        String name = "__md5(abc,abc)";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(name);
        String[] strings = null;
        while (matcher.find()){
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            strings = matcher.group(1).split(",");
        }
        if (strings.length > 1){
            String md5Hex = DigestUtils.md5Hex(strings[0] + strings[1]);
            return md5Hex;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getFunctionValue());
    }
}
