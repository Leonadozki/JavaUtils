package testng;

import com.domain.ParamBean;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     *  parallel属性：是否支持并行, 默认false串行
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
}
