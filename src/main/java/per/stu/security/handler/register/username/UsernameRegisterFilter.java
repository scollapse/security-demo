package per.stu.security.handler.register.username;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import per.stu.security.handler.login.username.UsernameAuthentication;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户名注册过滤器
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/25 16:06
 * @modified by
 */
public class UsernameRegisterFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(UsernameRegisterFilter.class);

    public UsernameRegisterFilter(AntPathRequestMatcher pathRequestMatcher,
                                        AuthenticationManager authenticationManager,
                                        AuthenticationSuccessHandler authenticationSuccessHandler,
                                        AuthenticationFailureHandler authenticationFailureHandler) {
        super(pathRequestMatcher);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        logger.debug(" --------- UsernameRegisterFilter.attemptAuthentication() ---------");
        // 提取请求数据
        String requestJsonData = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> requestMapData = JSON.parseObject(requestJsonData, Map.class);
        Optional<Map<String, Object>> optionalStringObjectMap = Optional.ofNullable(requestMapData);
        String username = optionalStringObjectMap.map(map -> map.get("username")).map(Object::toString).orElse(null);
        String password = optionalStringObjectMap.map(map -> map.get("password")).map(Object::toString).orElse(null);
        // 封装成Spring Security需要的对象
        UsernameRegister authentication = new UsernameRegister();
        authentication.setUsername(username);
        authentication.setPassword(password);
        authentication.setAuthenticated(false);

        // 开始登录认证。SpringSecurity会利用 Authentication对象去寻找 AuthenticationProvider进行登录认证
        return getAuthenticationManager().authenticate(authentication);
    }
}
