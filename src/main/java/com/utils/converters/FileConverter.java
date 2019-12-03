package com.utils.converters;

import com.github.crab2died.converter.ReadConvertible;
import com.github.crab2died.converter.WriteConvertible;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 *  入参文件内容读取为参数
 */
public class FileConverter implements WriteConvertible, ReadConvertible {

    @Override
    public Object execRead(String fileName) {
        // 先判断是否为特定文件
        if (StringUtils.endsWithIgnoreCase(fileName, "csv")){
            String path = System.getProperty("user.dir") + File.separator + "data"
                    + File.separator +fileName;
            try {
                String content = FileUtils.readFileToString(new File(path), "utf-8");
                // 打印文件内容
//                System.out.println(fileName + "文件内容: " + content);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    @Override
    public Object execWrite(Object o) {
        return null;
    }
}
