package com.utils;

import com.dao.IUserDaoImpl;
import com.domain.Api;
import com.domain.ParamBean;
import com.domain.User;
import com.github.crab2died.ExcelUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 *  Excel操作工具类
 */
public class ExcelUtil {

    private static String dir = System.getProperty("user.dir");
    private static long time = System.currentTimeMillis();
    private static String path = dir + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "data_"+ time +".xlsx";
    // 接口excel文件
    public static String path1 = dir + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "apitest6" +".xlsx";

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
     *  读excel为user对象
     */
    public static void readExcel2User() throws Exception {
        List<User> userList = ExcelUtils.getInstance().readExcel2Objects(path, User.class);
        for (User user: userList){
            System.out.println(user);
        }
    }

    /**
     *  读excel，返回api数组对象
     */
    public static List<Api> readExcel2Api() throws Exception {
        List<Api> apis = ExcelUtils.getInstance().readExcel2Objects(path1, Api.class);
        for (Api api: apis){
            System.out.println(api);
        }
        System.out.println("---------------------------------------");
        return apis;
    }

    /**
     * @return 读excel表内的关联参数并返回
     * @throws Exception
     */
    public static List<ParamBean> readExcel2ParamBean() throws Exception {
        List<ParamBean> paramBeans = ExcelUtils.getInstance().
                readExcel2Objects(path1, ParamBean.class, 1);
        for (ParamBean paramBean: paramBeans){
            // 反射获取对象值
            Field[] fields = paramBean.getClass().getDeclaredFields();
            for (Field field: fields){
                // 用BeanUtil简化获取属性值
                System.out.println("Field: " + field.getName() +
                        " 属性值： " + BeanUtils.getProperty(paramBean, field.getName()));
            }
        }
        System.out.println("---------------------------------------");
        return paramBeans;
    }

    public static void main(String[] args) throws Exception {
        readExcel2ParamBean();
    }
}
