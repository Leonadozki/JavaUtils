package com;

import com.domain.Api;
import com.domain.ParamBean;
import com.github.crab2died.ExcelUtils;
import com.utils.ExcelUtil;
import com.utils.MapUtil;
import com.utils.ParamUtil;
import com.utils.RequestsUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
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
     * 替换关联参数
     */
    public static void replace(Api api){
        // url参数化支持
        api.setUrl(ParamUtil.replace(api.getUrl()));
        // body参数化支持
        api.setParams(ParamUtil.replace(api.getParams()));
        // header参数化支持
        api.setHeaders(ParamUtil.replace(api.getHeaders()));

    }

    public static void main(String[] args) throws Exception {
        // 获取excel中所有接口信息
        List<Api> apis = ExcelUtil.readExcel2Api();
        // 获取sheet1中所有账号信息
        List<ParamBean> paramBeans = ExcelUtils.getInstance().
                readExcel2Objects(ExcelUtil.path1, ParamBean.class, 1);
        // 每次不同的账号信息做请求
        for (ParamBean paramBean: paramBeans){
            // 反射获取对象值
            Field[] fields = paramBean.getClass().getDeclaredFields();
            for (Field field: fields){
                // 用BeanUtil简化获取属性值
                System.out.println("Field: " + field.getName() +
                        " 属性值： " + BeanUtils.getProperty(paramBean, field.getName()));
                paramMap.put(field.getName(), BeanUtils.getProperty(paramBean, field.getName()));
            }

            // 请求部分
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
}
