package com;

import com.domain.Api;
import com.utils.ExcelUtil;
import com.utils.MapUtil;
import com.utils.RequestsUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  接口测试类
 */
public class ApiTest {

    static Map<String, String> paramMap = new LinkedHashMap<>();

    /**
     * @param api
     * @return 返回替换的关联参数
     */
    public static Api replace(Api api){
        // 先赋值测试一下参数化替换
        paramMap.put("id", "test");
        String regex = "\\$\\{(.+?)\\}";
        String url = api.getUrl();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()){
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            // 替换内容
            url = url.replace(matcher.group(), paramMap.get(matcher.group(1)));
        }
        System.out.println(url);
        api.setUrl(url);
        return api;
    }

    public static void main(String[] args) throws Exception {
        // 获取excel中所有接口信息
        List<Api> apis = ExcelUtil.readExcel2Api();
        for (Api api: apis){
            // 先判断是否启用
            if (api.getStatus() == 1){
                // 参数化替换
                replace(api);
                if ("get".equals(api.getMethod()) ){
                    RequestsUtil.doGet(api.getUrl());
                }else if ("post".equals(api.getMethod()) ){
                    // 入参转换为Map传入
                    RequestsUtil.doPost(api.getUrl(), MapUtil.convertString2Map1(api.getParams()));
                }else if("postjson".equals(api.getMethod()) ){
                    RequestsUtil.doPostJson(api.getUrl(), api.getParams(), MapUtil.convertString2Map2(api.getHeaders()));
                }
            }

        }
    }
}
