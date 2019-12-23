package com.domain;

import com.github.crab2died.annotation.ExcelField;

/**
 *  测试结果实体类
 */
public class TestResult extends Api{

    @ExcelField(title = "测试结果")
    private String testResult;

    @ExcelField(title = "数据库测试结果")
    private String dbTestResult;

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getDbTestResult() {
        return dbTestResult;
    }

    public void setDbTestResult(String dbTestResult) {
        this.dbTestResult = dbTestResult;
    }
}
