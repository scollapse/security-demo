package per.stu.security.handler.resourceapi.openapi;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import per.stu.exception.ExceptionTool;
import per.stu.model.vo.UserInfo;
import per.stu.util.JwtUtil;

import java.io.IOException;

/**
 * 类的描述
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/23 14:38
 * @modified by
 */
public class MyJwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyJwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("-------- MyJwtAuthenticationFilter.doFilterInternal() --------");

        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwtToken)) {
            ExceptionTool.throwException("miss.token","JWT token is missing!", HttpStatus.UNAUTHORIZED);
        }
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }
        try {
            UserInfo UserInfo = JwtUtil.verifyJwt(jwtToken, UserInfo.class);
            MyJwtAuthentication authentication = new MyJwtAuthentication();
            authentication.setJwtToken(jwtToken);
            authentication.setAuthenticated(true); // 设置true，认证通过。
            authentication.setCurrentUser(UserInfo);
            // 认证通过后，一定要设置到SecurityContextHolder里面去。
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (ExpiredJwtException e) {
            // 转换异常，指定code，让前端知道时token过期，去调刷新token接口
            ExceptionTool.throwException("token.expired","jwt过期", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionTool.throwException("token.invalid","jwt无效", HttpStatus.UNAUTHORIZED);
        }
        // 放行
        filterChain.doFilter(request, response);
    }
}
