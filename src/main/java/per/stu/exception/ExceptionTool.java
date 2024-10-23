package per.stu.exception;

import org.springframework.http.HttpStatus;

/**
 *  抛异常的工具类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/23 10:13
 * @modified by
 */
public class ExceptionTool {
    private static final HttpStatus defaultHttpStatus = HttpStatus.BAD_REQUEST;

    public static void throwException(String message) {
        throw new BaseException(message, defaultHttpStatus);
    }

    public static void throwException(boolean throwException, String message) {
        if (throwException) {
            throw new BaseException(message, defaultHttpStatus);
        }
    }

    public static void throwException(HttpStatus httpStatus) {
        throw new BaseException(httpStatus);
    }

    public static void throwException(String code , String message) {
        BaseException baseException = new BaseException(code,message, defaultHttpStatus);
        throw baseException;
    }

    public static void throwException(String message, HttpStatus httpStatus) {
        throw new BaseException(message, httpStatus);
    }

    public static void throwException(String code ,String message, HttpStatus httpStatus) {
        BaseException baseException = new BaseException(code,message, httpStatus);
        throw baseException;
    }
}
