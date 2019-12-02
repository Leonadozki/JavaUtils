package com.utils;

import java.util.Map;
import java.util.TreeMap;


/**
 *  字符串统计类
 */
public class WordCountUtil {

    /**
     *  统计字符串出现次数
     * */
    public static void wordCount(String target) {

        Map<Character,Integer> map = new TreeMap<>();

        // 第一种方法，用charAt()方法循环定位字符串
        //	另一种方法，用String对象的toCharArray()转化成char数组
        for (int i = 0; i < target.length(); i++) {
            char key = target.charAt(i);
            if (map.containsKey(key)) {
                map.put(key, map.get(key)+1);
                continue;
            }
            map.put(target.charAt(i), 1);
        }

        // 输出重复map数据
        for (Character key : map.keySet()) {
            if (map.get(key) > 1) {
                System.out.println("key值为：" + key + "; Value值为：" + map.get(key));;
            }
        }

    }

    public static void main(String[] args) {
        String testValue = "2342asfghgyu56asdasdaddddd";
        wordCount(testValue);
    }
}
