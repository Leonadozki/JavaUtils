package com.thread;

import com.ApiTest;
import com.domain.Api;
import com.domain.ParamBean;
import com.utils.ExcelUtil;
import com.utils.MapUtil;
import com.utils.ParamUtil;
import com.utils.RequestsUtil;

import java.util.List;

/**
 *  线程任务类实现请求逻辑
 */
public class ApiTask extends Thread{

    private ParamBean paramBean;

    /**
     * @param paramBean 构造方法传值
     */
    public ApiTask(ParamBean paramBean) {
        this.paramBean = paramBean;
    }

    @Override
    public void run() {
        try {
            // 获取excel中所有接口信息
            List<Api> apis = ExcelUtil.readExcel2Api();
            // 反射获取对象值，存入map
            ParamUtil.addFromObject(paramBean);
            // 请求部分
            for (Api api : apis) {
                // 先判断是否启用
                if (api.getStatus() == 1) {
                    // 参数化替换
                    ApiTest.replaceParams(api);
                    // 函数处理
                    ApiTest.replaceFunction(api);
                    String result = "";
                    if ("get".equals(api.getMethod())) {
                        result = RequestsUtil.doGet(api.getUrl());
                    } else if ("post".equals(api.getMethod())) {
                        // 入参转换为Map传入
                        result = RequestsUtil.doPost(api.getUrl(), MapUtil.convertString2Map1(api.getParams()));
                    } else if ("postjson".equals(api.getMethod())) {
                        result = RequestsUtil.doPostJson(api.getUrl(), api.getParams(), MapUtil.convertString2Map2(api.getHeaders()));
                    }
                    // 结果关联
                    ParamUtil.addCorrelationFromJson(result, api.getCorrelation());
                }
            }
            ParamUtil.map.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
