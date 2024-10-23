package per.stu.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import per.stu.model.entity.SysUser;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 测试类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 11:19
 * @modified by
 */
@SpringBootTest
public class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testInsertAndSelect() {
        SysUser user = SysUser.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        int insertResult = sysUserMapper.insert(user);
        assertThat(insertResult).isEqualTo(1);
        assertThat(user.getId()).isNotNull();

        SysUser fetchedUser = sysUserMapper.selectById(user.getId());
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getUsername()).isEqualTo("testuser");
    }
}
