package com.utils.converters;

import com.github.crab2died.converter.WriteConvertible;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  写excel日期格式化处理
 */
public class WriteConverter implements WriteConvertible {

    public Object execWrite(Object object) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (object instanceof Date){
            Date date = (Date)object;
            return df.format(date);
        }
        return object;
    }
}
