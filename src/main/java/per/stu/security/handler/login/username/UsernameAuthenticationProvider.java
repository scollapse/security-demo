package per.stu.security.handler.login.username;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import per.stu.constant.GlobalConstants;
import per.stu.model.dto.LoginUser;
import per.stu.model.vo.UserInfo;
import per.stu.security.handler.register.username.UsernameRegisterFilter;
import per.stu.util.RedisUtil;

/**
 * 用户名密码登录provider
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 18:07
 * @modified by
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UsernameAuthenticationProvider.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsernameAuthenticationProvider() {
        super();
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.debug("------- UsernameAuthenticationProvider.authenticate() -------");
        // 用户提交的用户名 + 密码：
        String username = (String)authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new BadCredentialsException("用户名或密码不能为空");
        }
        // 查数据库，匹配用户信息
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(username);
        if (loginUser == null
                || !passwordEncoder.matches(password, loginUser.getPassword())) {
            // 密码错误，直接抛异常。
            // 根据SpringSecurity框架的代码逻辑，认证失败时，应该抛这个异常：org.springframework.security.core.AuthenticationException
            // BadCredentialsException就是这个异常的子类
            // 抛出异常后后，AuthenticationFailureHandler的实现类会处理这个异常。
            throw new BadCredentialsException("用户名或密码不正确");
        }
        UserInfo userInfo = JSON.parseObject(JSON.toJSONString(loginUser.getSysUser()), UserInfo.class);
        // 从redis中获取token序列号并自增赋值
        userInfo.setTokenSeqNum(String.valueOf(RedisUtil.increment(GlobalConstants.REDIS_TOKEN_SEQ_NUM)));
        UsernameAuthentication token = new UsernameAuthentication();
        token.setCurrentUser(userInfo);
        token.setAuthenticated(true); // 认证通过，这里一定要设成true
        return token;
    }


    /*
     * @description 该方法用于判断该Provider是否支持传入的Authentication对象
     * @author syl
     * @date 2024/10/23 9:31
     * @param authentication
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernameAuthentication.class);
    }
}
