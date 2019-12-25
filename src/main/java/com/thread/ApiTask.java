package com.thread;

import com.domain.Api;
import com.domain.ParamBean;
import com.domain.TestResult;
import com.github.checkpoint.CheckPointUtils;
import com.utils.*;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *  线程任务类, 实现请求逻辑
 */
public class ApiTask extends Thread{

    private ParamBean paramBean;

    // 子线程都运行后再执行主线程
    private CountDownLatch latch;

    /**
     * @param paramBean 构造方法传值
     */
    public ApiTask(ParamBean paramBean) {
        this.paramBean = paramBean;
    }

    /**
     * @param paramBean 传用户
     * @param latch 线程计数器
     */
    public ApiTask(ParamBean paramBean, CountDownLatch latch){
        this.paramBean = paramBean;
        this.latch = latch;
    }

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

    @Override
    public void run() {
        try {
            // 存放结果及对应数据
            List<TestResult> runTestResult = new ArrayList<>();
            // 获取excel中所有接口信息
            List<Api> apis = ExcelUtil.readExcel2Api();
            // 反射获取对象值，存入map
            ParamUtil.addFromObject(paramBean);
            // 请求部分
            for (Api api : apis) {
                // 先判断是否启用
                if (api.getStatus() == 1) {
                    System.out.println("-----------当前用例： " + api.getName() + "----------");
                    // 参数化替换
                    replaceParams(api);
                    // 函数处理
                    replaceFunction(api);
                    String result = "";
                    if ("get".equals(api.getMethod())) {
                        result = RequestsUtil.doGet(api.getUrl());
                    } else if ("post".equals(api.getMethod())) {
                        // 入参转换为Map传入
                        result = RequestsUtil.doPost(api.getUrl(), MapUtil.convertString2Map1(api.getParams()));
                    } else if ("postJson".equalsIgnoreCase(api.getMethod())) {
                        result = RequestsUtil.doPostJson(api.getUrl(), api.getParams(), MapUtil.convertString2Map2(api.getHeaders()));
                    }
                    // 结果关联
                    ParamUtil.addCorrelationFromJson(result, api.getCorrelation());
                    // 检查点验证结果, getMsg()方法输出语义结果
                    String checkResult = CheckPointUtils.check(result, api.getCheckPoint()).getMsg();
                    System.out.println("检查点： " + checkResult);
                    // 结果处理
                    TestResult testResult = new TestResult();
                    testResult.setTestResult(checkResult);
                    BeanUtils.copyProperties(testResult, api);
                    runTestResult.add(testResult);
                }
            }
            // 添加了测试结果的请求类对象添加数组
            ApiThreadTest.listResult.addAll(runTestResult);
            ParamUtil.threadLocal.get().clear();
            // 运行完毕，计数减1
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
