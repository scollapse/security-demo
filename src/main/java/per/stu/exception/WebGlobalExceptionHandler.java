package per.stu.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import per.stu.model.vo.Result;
import org.springframework.security.core.AuthenticationException;
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
        return Result.fail("internal.server.error",e.getMessage());
    }

    @ExceptionHandler(value = BaseException.class)
    public Result handleException(HttpServletResponse response, BaseException e) {
        log.error("自定义业务异常捕捉", e);
        response.setStatus(e.getStatus().value());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 处理认证异常
    @ExceptionHandler(AuthenticationException.class)
    public Result handleAuthenticationException(AuthenticationException e, HttpServletResponse response) {
        log.error("认证异常捕捉", e);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return Result.fail("authentication.error", e.getMessage());
    }
}
