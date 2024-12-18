package per.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import per.stu.model.entity.SysUser;

/**
 * user mapper
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/22 11:15
 * @modified by
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    // 根据用户名查询用户信息
    SysUser selectByUsername(String username);

    void addUser(SysUser addUser);
}
