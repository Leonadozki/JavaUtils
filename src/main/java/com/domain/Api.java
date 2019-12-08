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

    @ExcelField(title = "头部")
    private String headers;

    @ExcelField(title = "关联")
    private String correlation;

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

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
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
                ", headers='" + headers + '\'' +
                ", correlation='" + correlation + '\'' +
                '}';
    }
}
