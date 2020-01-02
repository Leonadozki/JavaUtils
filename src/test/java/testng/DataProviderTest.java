package testng;

import com.domain.ParamBean;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @DataProvider用法
 */
public class DataProviderTest {

    /**
     *  List<object>方式
     */
    @Test(dataProvider = "param", threadPoolSize = 2)
    public static void testList(ParamBean paramBean){
        System.out.println(Thread.currentThread().getName());
        System.out.println(paramBean.getLoginname());
        System.out.println(paramBean.getLoginpass());
    }

    /**
     *  Iterator<Object[]>方式，parallel属性：是否支持并行, 默认false串行
     */
    @DataProvider(name = "param")
    public static Iterator<Object[]> paramProvider(){
        List<Object[]> dataProvider = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ParamBean paramBean = new ParamBean();
            paramBean.setLoginname("testName");
            paramBean.setLoginpass(i + "" + "" + i + "" + i);
            dataProvider.add(new Object[] {paramBean});
        }
        return dataProvider.iterator();
    }

    /**
     *  Object[][]方式
     */
    @Test(dataProvider = "param1")
    public static void testArray(Class clz, String name, String alpha, Integer integer){
        System.out.println(Thread.currentThread().getName());
        System.out.println("class: " + clz);
        System.out.println("name: " + name);
    }

    /**
     *  Object[][]方式，parallel属性：是否支持并行, 默认false串行
     */
    @DataProvider(name = "param1")
    public Object[][] paramProviderArray(){
        return new Object[][]{
                {Vector.class, "test1", "a", 1},
                {String.class, "test2", "b", 2},
                {Integer.class, "test3", "c", 3}
        };
    }

}
