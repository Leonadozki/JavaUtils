package com.domain;

import com.github.crab2died.annotation.ExcelField;
import com.utils.converters.FileConverter;
import com.utils.converters.ReadConverter;

/**
 *  接口实体类
 */
public class Api {

    private Integer id;

    @ExcelField(title = "用例名称")
    private String name;

    @ExcelField(title = "地址")
    private String url;

    @ExcelField(title = "类型")
    private String method;

    @ExcelField(title = "是否开启", readConverter = ReadConverter.class)
    private Integer status;

    @ExcelField(title = "参数", readConverter = FileConverter.class)
    private String params;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Api{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", status=" + status +
                ", params='" + params + '\'' +
                '}';
    }
}
