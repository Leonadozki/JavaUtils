package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.domain.Api;
import com.domain.ParamBean;
import com.github.crab2died.ExcelUtils;
import com.utils.ExcelUtil;
import com.utils.MapUtil;
import com.utils.ParamUtil;
import com.utils.RequestsUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  接口测试类
 */
public class ApiTest {


    /**
     * @param api
     * 参数化替换
     */
    public static void replace(Api api){
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
     *  结果关联处理
     */
    public static void addCorrelation(String respJson, String correlation){
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
            System.out.println("global map: "+ParamUtil.map);
            // 初始化关联表
            correlate_map.clear();
        }
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
//                // 用BeanUtil简化获取属性值
                System.out.println("Field: " + field.getName() +
                        " 属性值： " + BeanUtils.getProperty(paramBean, field.getName()));
                ParamUtil.map.put(field.getName(), BeanUtils.getProperty(paramBean, field.getName()));
            }

            // 请求部分
            for (Api api: apis){
                // 先判断是否启用
                if (api.getStatus() == 1){
                    // 参数化替换
                    replace(api);
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
                    addCorrelation(result, api.getCorrelation());
                }
            }
            ParamUtil.map.clear();
        }
    }
}
