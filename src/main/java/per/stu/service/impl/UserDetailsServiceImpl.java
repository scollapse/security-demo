package per.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import per.stu.mapper.SysUserMapper;
import per.stu.model.dto.LoginUser;
import per.stu.model.entity.SysUser;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("------- UserDetailsServiceImpl.loadUserByUsername() -------");
        return new LoginUser(SysUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .email("test@email.com")
                .role("ADMIN")
                .phoneNumber("1234567890")
                .status(true)
                .build());
//        //根据用户名查询账号信息
//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
//        // 判断用户是否存在
//        if (Objects.isNull(sysUser)) {
//            throw new UsernameNotFoundException("用户不存在");
//        }
//        // 判断密码是否正确
//        // 返回UserDetails对象
//        return new LoginUser(sysUser);
    }
}
