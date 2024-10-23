package per.stu.security.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import per.stu.security.handler.exception.CustomAccessDeniedHandler;
import per.stu.security.handler.exception.CustomAuthenticationEntryPoint;
import per.stu.security.handler.login.LoginFailHandler;
import per.stu.security.handler.login.LoginSuccessHandler;
import per.stu.security.handler.login.username.UsernameAuthenticationFilter;
import per.stu.security.handler.login.username.UsernameAuthenticationProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置请求的授权规则
        http
            .securityMatcher("/user/login/**", "/user/register")
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            );

        // 配置自定义的filter
        http
        .addFilterBefore(usernameLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // 配置自定义的 AuthenticationEntryPoint 和 AccessDeniedHandler
        http
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        // 禁用不常用的过滤器
        http

                .httpBasic(httpBasic -> httpBasic.disable())    // 禁用 HTTP Basic 认证
                .formLogin(formLogin -> formLogin.disable())    // 禁用表单登录
                .csrf(csrf -> csrf.disable())                    // 禁用 CSRF 保护
                .logout(logout -> logout.disable())              // 禁用 Spring 默认的注销功能
                .sessionManagement(session -> session.disable()) // 禁用会话管理（无状态应用）
                .requestCache(requestCache -> requestCache.disable()) // 禁用请求缓存过滤器
                .securityContext(securityContext -> securityContext.disable()) // 禁用安全上下文过滤器
                .anonymous(anonymous -> anonymous.disable());    // 禁用匿名用户过滤器
        return http.build();
    }


    //PasswordEncoder的实现类为BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*
     * @description 用户名密码登录过滤器
     * @author syl
     * @date 2024/10/23 11:09
     * @param
     * @return per.stu.security.handler.login.username.UsernameAuthenticationFilter
     */
    @Bean
    public UsernameAuthenticationFilter usernameLoginFilter () {
        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);
        return new UsernameAuthenticationFilter(
                new AntPathRequestMatcher("/user/login/username", HttpMethod.POST.name()),
                new ProviderManager(
                        List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))),
                loginSuccessHandler,
                loginFailHandler);
    }



}
