package per.stu.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import per.stu.model.Result;

/**
 * 全局异常捕捉
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/17 14:34
 * @modified by
 */
@RestControllerAdvice
public class WebGlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result handleException(HttpServletResponse response, Exception e) {
        log.error("全局异常捕捉", e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new Result("500", e.getMessage(), null);
    }
}
