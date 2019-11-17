package com.utils;

import com.dao.IUserDaoImpl;
import com.domain.User;
import com.github.crab2died.ExcelUtils;

import java.io.File;
import java.util.List;

/**
 *  Excel操作工具类
 */
public class ExcelUtil {

    private static String dir = System.getProperty("user.dir");
    private static long time = System.currentTimeMillis();
    private static String path = dir + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "data_"+ time +".xlsx";

    /**
     *  写excel操作
     */
    public static void writeExcel() throws Exception {
        IUserDaoImpl userDao = new IUserDaoImpl();
        List<User> users = userDao.listUsers();
        // 常用参数： list数据、实体类对象和写文件path
        ExcelUtils.getInstance().exportObjects2Excel(users, User.class, path);
    }

    /**
     *  读excel操作
     */
    public static void readExcel() throws Exception {
        List<User> userList = ExcelUtils.getInstance().readExcel2Objects(path, User.class);
        for (User user: userList){
            System.out.println(user);
        }
    }

    public static void main(String[] args) throws Exception {
        writeExcel();
        readExcel();
    }
}
