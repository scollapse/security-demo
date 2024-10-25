package per.stu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import per.stu.exception.ExceptionTool;
import per.stu.mapper.SysUserMapper;
import per.stu.model.dto.LoginUser;
import per.stu.model.entity.SysUser;
import per.stu.security.handler.login.username.UsernameAuthenticationProvider;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * UserDetailsService实现类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 11:50
 * @modified by
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("------- UserDetailsServiceImpl.loadUserByUsername() -------");
        //根据用户名查询账号信息
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        // 判断用户是否存在
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 返回UserDetails对象
        return new LoginUser(sysUser);
    }

    public void addUser(String username, String password) {
        try {
            SysUser sysUser = sysUserMapper.selectByUsername(username);
            if (sysUser != null) {
                ExceptionTool.throwException("用户已存在");
            }
            SysUser addUser = SysUser.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            sysUserMapper.addUser(addUser);
        } catch (Exception e) {
            logger.error("注册失败", e);
            ExceptionTool.throwException("注册失败");
        }
    }
}
