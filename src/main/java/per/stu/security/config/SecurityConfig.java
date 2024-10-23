package per.stu.security.config;

import jakarta.servlet.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import per.stu.security.handler.exception.CustomAccessDeniedHandler;
import per.stu.security.handler.exception.CustomAuthenticationEntryPoint;
import per.stu.security.handler.exception.CustomSecurityExceptionHandler;
import per.stu.security.handler.login.LoginFailHandler;
import per.stu.security.handler.login.LoginSuccessHandler;
import per.stu.security.handler.login.resourceapi.openapi.MyJwtAuthenticationFilter;
import per.stu.security.handler.login.username.UsernameAuthenticationFilter;
import per.stu.security.handler.login.username.UsernameAuthenticationProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    private final AuthenticationEntryPoint authenticationExceptionHandler = new CustomAuthenticationEntryPoint();
    private final AccessDeniedHandler authorizationExceptionHandler = new CustomAccessDeniedHandler();
    private final Filter customSecurityExceptionHandler = new CustomSecurityExceptionHandler();

    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //PasswordEncoder的实现类为BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void commonHttpSetting(HttpSecurity http) throws Exception {
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

        // 配置自定义的 AuthenticationEntryPoint 和 AccessDeniedHandler
        http
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 认证失败异常
                        .authenticationEntryPoint(authenticationExceptionHandler)
                        // 鉴权失败异常
                        .accessDeniedHandler(authorizationExceptionHandler)
                );
        // 其他未知异常. 尽量提前加载。
        http.addFilterBefore(customSecurityExceptionHandler, SecurityContextHolderFilter.class);
    }


    /*
     * @description 登录过滤器链，配置登录请求的授权规则，配置自定义的filter，配置自定义的 AuthenticationEntryPoint 和 AccessDeniedHandler
     * @author syl
     * @date 2024/10/23 14:28
     * @param http
     * @return org.springframework.security.web.SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        // 配置请求的授权规则
        http
            .securityMatcher("/user/login/**")
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            );
        // 配置自定义的filter
        http
        .addFilterBefore(usernameLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /*
     * @description 业务接口过滤器链，配置业务接口请求的授权规则，配置自定义的filter
     * @author syl
     * @date 2024/10/23 14:48
     * @param http
     * @return org.springframework.security.web.SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain businessApiFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        // 配置请求的授权规则
        http
                .securityMatcher("/open-api/business-1")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                );
        // 配置自定义的filter
        http
                .addFilterBefore(new MyJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
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
