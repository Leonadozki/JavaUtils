package com.domain;

import com.github.crab2died.annotation.ExcelField;

/**
 *  参数化实体类
 */
public class ParamBean {

    @ExcelField(title = "loginname")
    private String loginname;

    @ExcelField(title = "loginpass")
    private String loginpass;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpass() {
        return loginpass;
    }

    public void setLoginpass(String loginpass) {
        this.loginpass = loginpass;
    }

    @Override
    public String toString() {
        return "ParamBean{" +
                "loginname='" + loginname + '\'' +
                ", loginpass='" + loginpass + '\'' +
                '}';
    }
}
