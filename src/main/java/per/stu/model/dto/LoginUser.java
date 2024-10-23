package per.stu.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import per.stu.model.entity.SysUser;

import java.util.Collection;
import java.util.List;

/**
 * 登录用户
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 11:46
 * @modified by
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private String sessionId; // 会话id，全局唯一

    private SysUser sysUser;

    public LoginUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of((GrantedAuthority) () -> sysUser.getRole());
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return sysUser.getStatus();
    }
}
