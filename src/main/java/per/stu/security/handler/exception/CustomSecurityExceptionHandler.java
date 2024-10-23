package per.stu.security.handler.exception;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import per.stu.exception.BaseException;
import per.stu.model.vo.Result;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/23 15:22
 * @modified by
 */
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {
    public static final Logger logger = LoggerFactory.getLogger(
            CustomSecurityExceptionHandler.class);


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            handleException(response, e.getMessage(), e.getCode(), e.getStatus().value());
        } catch (AuthenticationException | AccessDeniedException e) {
            handleException(response, e.getMessage(), "forbidden", HttpStatus.FORBIDDEN.value());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            handleException(response, "System Error", "system.error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private void handleException(HttpServletResponse response, String message, String code, int status) throws
            IOException {
        Result result = Result.builder()
                .message(message)
                .code(code)
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(result));
        }
    }

}
