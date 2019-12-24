package com.thread;

import com.domain.ParamBean;
import com.domain.TestResult;
import com.github.crab2died.ExcelUtils;
import com.utils.ExcelUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *  Api线程测试类
 */
public class ApiThreadTest {

    public static List<TestResult> listResult = new ArrayList<>();

    // 格式化当前时间
    private static String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd");
        return dateFormat.format(new Date());
    }

    public static void main(String[] args) throws Exception {
        // 获取sheet1中所有账号信息
        List<ParamBean> paramBeans = ExcelUtils.getInstance().
                readExcel2Objects(ExcelUtil.path1, ParamBean.class, 1);
        // 线程计数器，传入账号数量
        CountDownLatch latch = new CountDownLatch(paramBeans.size());
        for (ParamBean paramBean: paramBeans){
            ApiTask task = new ApiTask(paramBean, latch);
            System.out.println("----当前线程用户"+ paramBean.getLoginname() + "----");
            task.start();
        }
        // await()阻塞，直到countDown()执行，数量减1
        latch.await(3, TimeUnit.MINUTES);
        // 结果写excel
        File resultPath = new File(ExcelUtil.path_result);
        if (!resultPath.exists()){
            resultPath.mkdirs();
        }
        String resultFile = ExcelUtil.path_result + "result_" + getDate() + ".xlsx";
        ExcelUtils.getInstance().exportObjects2Excel(listResult, TestResult.class, resultFile);
    }


}
