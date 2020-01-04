package testng;

import com.ApiTest;
import com.domain.Api;
import com.domain.ParamBean;
import com.domain.TestResult;
import com.github.checkpoint.CheckPointUtils;
import com.github.crab2died.ExcelUtils;
import com.mail.EmailUtils;
import com.utils.ExcelUtil;
import com.utils.MapUtil;
import com.utils.ParamUtil;
import com.utils.RequestsUtil;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.thread.ApiThreadTest.getDate;
import static com.thread.ApiThreadTest.listResult;

/**
 *  TestNg整合接口测试
 */
public class ApiTestNg {

    private static Logger logger = LoggerFactory.getLogger(ApiTestNg.class);

    @AfterClass
    public void afterClass(){
        try {
            // 结果写excel
            File resultPath = new File(ExcelUtil.path_result);
            if (!resultPath.exists()){
                resultPath.mkdirs();
            }
            String resultFile = ExcelUtil.path_result + "result_" + getDate() + ".xlsx";
            ExcelUtils.getInstance().exportObjects2Excel(listResult, TestResult.class, resultFile);
            // 结果附件上邮件
            System.out.println("邮件发送...");
                EmailUtils.sendMsg(resultFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("结果处理");
            logger.error("error" + e);
        }
    }

    /**
     * 主测试方法
     * @param paramBean 多账号信息
     */
    @Epic("并行单元测试")
    @Story("单元测试")
    @Test(dataProvider = "accountProvider", threadPoolSize = 5)
    public void testcase(ParamBean paramBean){
        // 当前线程记录
        System.out.println(Thread.currentThread().getName());
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
                    ApiTest.replaceParams(api);
                    // 函数处理
                    ApiTest.replaceFunction(api);
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
            listResult.addAll(runTestResult);
            ParamUtil.threadLocal.get().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Iterator<Object[]> 方式提供数据
     */
    @DataProvider(name = "accountProvider")
    public Iterator<Object[]> accountProvider(){
        List<Object[]> dataProvider = new ArrayList<>();
        // 获取sheet1中所有账号信息
        try {
            List<ParamBean> paramBeans = ExcelUtils.getInstance().
                    readExcel2Objects(ExcelUtil.path1, ParamBean.class, 1);
            for (ParamBean paramBean: paramBeans){
                dataProvider.add(new Object[]{paramBean});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataProvider.iterator();
    }
}
