package per.stu.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.stu.model.entity.SysUser;

import java.time.LocalDateTime;

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
@AllArgsConstructor
public class UserInfo {

    private String sessionId; // 会话id，全局唯一

    // token序列号
    private String tokenSeqNum;

    // token批次号
    private String tokenBatchNum;

    private Long id; // 用户唯一ID

    private String username; // 用户名，唯一

    private String email; // 邮箱地址，唯一

    private String phoneNumber; // 电话号码（可选）

    private String role; // 用户角色，默认是 "USER"

    private Boolean status; // 用户状态，默认 1 表示正常

    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间

    private LocalDateTime lastLogin; // 最后一次登录时间

    private Integer failedAttempts; // 登录失败次数

    private Boolean accountLocked; // 帐号是否被锁定

    private LocalDateTime accountExpiration; // 帐号过期时间

    private LocalDateTime passwordExpiration; // 密码过期时间

}
