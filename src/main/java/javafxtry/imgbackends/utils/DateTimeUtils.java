package javafxtry.imgbackends.utils;
/**
 * @ClassName DateTimeUtils
 * @Description Date time utility
 * @Author Zhenyu YE
 * @Date 2019/12/12 21:09
 * @Version 1.0
 **/
public class DateTimeUtils {

    public static String getCurrentDateTime(){
        return String.valueOf(System.nanoTime());
    }
}
