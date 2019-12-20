package com;

import com.domain.Api;
import com.domain.ParamBean;
import com.github.crab2died.ExcelUtils;
import com.utils.*;

import java.util.List;

/**
 *  接口测试类
 */
public class ApiTest {


    /**
     * @param api
     * 参数化替换
     */
    public static void replaceParams(Api api){
        // url参数化支持
        System.out.println("replace url: " + ParamUtil.replace(api.getUrl()));
        api.setUrl(ParamUtil.replace(api.getUrl()));
        // body参数化支持
        System.out.println("replace params: " + ParamUtil.replace(api.getParams()));
        api.setParams(ParamUtil.replace(api.getParams()));
        // header参数化支持
        System.out.println("replace headers: " + ParamUtil.replace(api.getHeaders()));
        api.setHeaders(ParamUtil.replace(api.getHeaders()));

    }

    /**
     *  函数处理替换
     *  如：#{__uuid}
     */
    public static void replaceFunction(Api api){
        // url函数处理支持
        api.setUrl(FunctionUtil.replace(api.getUrl()));
        // body函数处理支持
        api.setParams(FunctionUtil.replace(api.getParams()));
        // header函数处理支持
        api.setHeaders(FunctionUtil.replace(api.getHeaders()));
    }


    public static void main(String[] args) throws Exception {
        // 获取excel中所有接口信息
        List<Api> apis = ExcelUtil.readExcel2Api();
        // 获取sheet1中所有账号信息
        List<ParamBean> paramBeans = ExcelUtils.getInstance().
                readExcel2Objects(ExcelUtil.path1, ParamBean.class, 1);
        // 每次不同的账号信息做请求
        for (ParamBean paramBean: paramBeans){
            // 反射获取对象值，存入map
            ParamUtil.addFromObject(paramBean);
            // 请求部分
            for (Api api: apis){
                // 先判断是否启用
                if (api.getStatus() == 1){
                    // 参数化替换
                    replaceParams(api);
                    // 函数处理
                    replaceFunction(api);
                    String result = "";
                    if ("get".equals(api.getMethod()) ){
                        result = RequestsUtil.doGet(api.getUrl());
                    }else if ("post".equals(api.getMethod()) ){
                        // 入参转换为Map传入
                        result = RequestsUtil.doPost(api.getUrl(), MapUtil.convertString2Map1(api.getParams()));
                    }else if("postjson".equals(api.getMethod()) ){
                        result = RequestsUtil.doPostJson(api.getUrl(), api.getParams(), MapUtil.convertString2Map2(api.getHeaders()));
                    }
                    // 结果关联
                    ParamUtil.addCorrelationFromJson(result, api.getCorrelation());
                }
            }
//            ParamUtil.map.clear();
        }
    }
}
