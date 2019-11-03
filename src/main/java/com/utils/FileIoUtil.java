package com.utils;


import com.dao.IUserDaoImpl;
import com.domain.User;

import java.io.File;
import java.util.List;

public class FileIoUtil {

    public static void main(String[] args) {

        IUserDaoImpl userDao = new IUserDaoImpl();
        List<User> users = userDao.listUsers();
        String dir = System.getProperty("user.dir");
        String path = dir + File.separator + "";
        System.out.println(path);
    }
}
