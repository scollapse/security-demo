package per.stu.security.handler.register.username;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import per.stu.exception.BaseException;
import per.stu.security.handler.login.username.UsernameAuthentication;
import per.stu.service.impl.UserDetailsServiceImpl;

/**
 *  用户名注册认证提供者
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/25 16:12
 * @modified by
 */
@Component
public class UsernameRegisterProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UsernameRegisterProvider.class);

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsernameRegisterProvider() {
        super();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException,BaseException{
        logger.debug("------- UsernameRegisterProvider.authenticate() -------");
        // 用户提交的用户名 + 密码：
        String username = (String)authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            // 用户名或密码为空
            // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
            // 抛出异常后后，AuthenticationFailureHandler的实现类会处理这个异常。
            throw new InternalAuthenticationServiceException("用户名或密码不能为空");
        }
        userDetailsService.addUser(username, password);
        UsernameAuthentication token = new UsernameAuthentication();
        token.setAuthenticated(true);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernameRegister.class);
    }
}
