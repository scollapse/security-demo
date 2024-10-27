package per.stu.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/23 11:03
 * @modified by
 */
public class DateUtil {

    public static Date nowDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long nowMilli() {
        return System.currentTimeMillis();
    }

    // 分钟转秒
    public static long minuteToSecond(long minute) {
        return minute * 60;
    }
}
