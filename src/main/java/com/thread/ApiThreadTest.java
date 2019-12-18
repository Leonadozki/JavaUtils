package com.thread;

import com.domain.ParamBean;
import com.github.crab2died.ExcelUtils;
import com.utils.ExcelUtil;

import java.util.List;

public class ApiThreadTest {

    public static void main(String[] args) throws Exception {
        // 获取sheet1中所有账号信息
        List<ParamBean> paramBeans = ExcelUtils.getInstance().
                readExcel2Objects(ExcelUtil.path1, ParamBean.class, 1);
        for (ParamBean paramBean: paramBeans){
            ApiTask task = new ApiTask(paramBean);
            System.out.println("----当前线程用户"+ paramBean.getLoginname() + "----");
            task.start();
        }
    }


}
