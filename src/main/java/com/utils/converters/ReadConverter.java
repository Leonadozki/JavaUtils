package com.utils.converters;

import com.github.crab2died.converter.ReadConvertible;

public class ReadConverter implements ReadConvertible {

    /**
     * @param s 是否开启
     * @return 1已开启， 0未开启
     */
    public Object execRead(String s) {
        if ("是".equals(s)){
            return 1;
        }
        return 0;
    }
}
