package per.stu.security.handler.login;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import per.stu.exception.ExceptionTool;
import per.stu.model.vo.UserInfo;
import per.stu.model.vo.Result;
import per.stu.util.DateUtil;
import per.stu.util.JwtUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证成功/登录成功 事件处理器
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/23 10:02
 * @modified by
 */
@Component
public class LoginSuccessHandler extends
        AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public LoginSuccessHandler() {
        this.setRedirectStrategy(new RedirectStrategy() {
            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
                    throws IOException {
                // 更改重定向策略，前后端分离项目，后端使用RestFul风格，无需做重定向
                // Do nothing, no redirects in REST
            }
        });
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("------- LoginSuccessHandler.onAuthenticationSuccess() -------");
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserInfo)) {
            ExceptionTool.throwException("Login successful, but principal is not of type LoginUser!");
        }
        UserInfo currentUser = (UserInfo) principal;
        currentUser.setSessionId(UUID.randomUUID().toString());

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("token", generateToken(currentUser));
        responseData.put("refreshToken", generateRefreshToken(currentUser));

        Object details = authentication.getDetails();
        if (details instanceof Map) {
            responseData.putAll((Map) details);
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSON.toJSONString(Result.success("Login successful!", responseData)));
        }
    }

    public String generateToken(UserInfo currentUser) {
        long expiredTime = DateUtil.nowMilli() + TimeUnit.MINUTES.toMillis(10); // 10分钟后过期
//        currentUser.setExpiredTime(expiredTime);
        return JwtUtil.generateToken(currentUser, expiredTime);
    }

    private String generateRefreshToken(UserInfo loginInfo) {
        return JwtUtil.generateToken(loginInfo, DateUtil.nowMilli() + TimeUnit.DAYS.toMillis(30));
    }
}
