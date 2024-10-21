package per.stu.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义业务基础异常
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/21 13:50
 * @modified by
 */
public class BaseException extends RuntimeException {

    private HttpStatus status;

    private String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BaseException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.code = String.valueOf(status.value());
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
