package per.stu.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.stu.constant.GlobalConstants;
import per.stu.model.vo.Result;
import per.stu.model.vo.UserInfo;
import per.stu.util.DateUtil;
import per.stu.util.RedisUtil;

import java.util.concurrent.TimeUnit;

import static per.stu.service.impl.UserDetailsServiceImpl.getUserInfo;


/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * logout
     * @return
     */
    @RequestMapping("/logout")
    public Result logout() {
        UserInfo userInfo = getUserInfo();
        if (userInfo == null) {
            return Result.fail("logout.fail","用户未登录");
        }
        // 将token放进黑名单
        RedisUtil.set(GlobalConstants.REDIS_TOKEN_BLACKLIST+userInfo.getUsername()+userInfo.getTokenSeqNum(), "",DateUtil.minuteToSecond(10));
        return Result.success("logout.success", "退出成功");
    }

}
