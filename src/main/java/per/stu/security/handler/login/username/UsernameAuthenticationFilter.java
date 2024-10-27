package per.stu.security.handler.login.username;

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
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * * 用户名密码登录
 * * AbstractAuthenticationProcessingFilter 的实现类要做的工作：
 * * 1. 从HttpServletRequest提取授权凭证。假设用户使用 用户名/密码 登录，就需要在这里提取username和password。
 * *    然后，把提取到的授权凭证封装到的Authentication对象，并且authentication.isAuthenticated()一定返回false
 * * 2. 将Authentication对象传给AuthenticationManager进行实际的授权操作
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 18:26
 * @modified by
 */
public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(UsernameAuthenticationFilter.class);

    public UsernameAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
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
        logger.debug(" --------- UsernameAuthenticationFilter.attemptAuthentication() ---------");
        // 提取请求数据
        String requestJsonData = request.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> requestMapData = JSON.parseObject(requestJsonData, Map.class);
        Optional<Map<String, Object>> optionalStringObjectMap = Optional.ofNullable(requestMapData);
        String username = optionalStringObjectMap.map(map -> map.get("username")).map(Object::toString).orElse(null);
        String password = optionalStringObjectMap.map(map -> map.get("password")).map(Object::toString).orElse(null);
        // 封装成Spring Security需要的对象
        UsernameAuthentication authentication = new UsernameAuthentication();
        authentication.setUsername(username);
        authentication.setPassword(password);
        authentication.setAuthenticated(false);

        // 开始登录认证。SpringSecurity会利用 Authentication对象去寻找 AuthenticationProvider进行登录认证
        return getAuthenticationManager().authenticate(authentication);
    }
}
