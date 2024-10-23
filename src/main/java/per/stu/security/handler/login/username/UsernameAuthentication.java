package per.stu.security.handler.login.username;

import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import per.stu.model.dto.LoginUser;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * 类的描述
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 17:31
 * @modified by
 */
public class UsernameAuthentication extends AbstractAuthenticationToken {

    private String username;// 前端传过来
    private String password; // 前端传过来
    private LoginUser currentUser; // 认证成功后，后台从数据库获取信息

    public UsernameAuthentication() {
        // 权限，用不上，直接null
        super(null);
    }

    @Override
    public Object getCredentials() {
        // 根据SpringSecurity的设计，授权成后，Credential（比如，登录密码）信息需要被清空
        return isAuthenticated() ? null : password;
    }

    @Override
    public Object getPrincipal() {
        // 根据SpringSecurity的设计，授权成功之前，getPrincipal返回的客户端传过来的数据。授权成功后，返回当前登陆用户的信息
        return isAuthenticated() ? currentUser : username;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginUser currentUser) {
        this.currentUser = currentUser;
    }
}
