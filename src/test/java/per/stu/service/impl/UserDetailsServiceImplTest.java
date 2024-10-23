package per.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import per.stu.mapper.SysUserMapper;
import per.stu.model.vo.UserInfo;
import per.stu.model.entity.SysUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * UserDetailsServiceImplTest
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 18:12
 * @modified by
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private SysUser sysUser;

    @BeforeEach
    void setUp() {
        sysUser = new SysUser();
        sysUser.setUsername("testuser");
        sysUser.setPassword("password");
    }


    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistentuser");
        });
    }
}
