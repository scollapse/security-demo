package per.stu.security.handler.register;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import per.stu.model.vo.Result;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 注册失败处理器
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/25 17:22
 * @modified by
 */
@Component
public class RegisterFailHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            Result responseData = Result.fail("Register.fail", exception.getMessage());
            writer.print(JSON.toJSONString(responseData));
        }
    }
}
