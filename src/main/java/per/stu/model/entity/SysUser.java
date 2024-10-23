package per.stu.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id; // 用户唯一ID

    private String username; // 用户名，唯一

    private String password; // 用户密码（应加密存储）

    private String email; // 邮箱地址，唯一

    private String phoneNumber; // 电话号码（可选）

    private String role = "USER"; // 用户角色，默认是 "USER"

    private Boolean status = true; // 用户状态，默认 1 表示正常

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

    private LocalDateTime lastLogin; // 最后一次登录时间

    private Integer failedAttempts = 0; // 登录失败次数

    private Boolean accountLocked = false; // 帐号是否被锁定

    private LocalDateTime accountExpiration; // 帐号过期时间

    private LocalDateTime passwordExpiration; // 密码过期时间
}
