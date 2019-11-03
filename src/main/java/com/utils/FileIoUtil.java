package com.utils;


import com.dao.IUserDaoImpl;
import com.domain.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileIoUtil {

    public static void main(String[] args) {

        IUserDaoImpl userDao = new IUserDaoImpl();
        List<User> users = userDao.listUsers();
        String dir = System.getProperty("user.dir");
        String path = dir + File.separator + "src" + File.separator + "main"
                + File.separator + "resources" + File.separator + "data.txt";
        System.out.println(path);
        // 写文件
        File file = new File(path);
        try {
            FileUtils.writeLines(file, "utf-8", users, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
