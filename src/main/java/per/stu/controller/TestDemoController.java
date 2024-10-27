package per.stu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.stu.exception.BaseException;
import per.stu.model.vo.UserInfo;
import per.stu.model.vo.Result;

/**
 * 测试控制类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/17 14:19
 * @modified by
 */
@RestController
@RequestMapping("/api")
public class TestDemoController {



    @GetMapping("/business-1")
    public Result getA(){
        UserInfo principal = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result("200", "成功", principal,true);
    }

    @GetMapping("/business-2")
    public Result getB(){
        if (true) {
            throw new RuntimeException("业务2异常");
        }
        return new Result("200", "成功", "业务2",true);
    }

    @GetMapping("/business-3")
    public Result getC(){
        if (true) {
            String code = "token.invalid";
            String message = "token无效";
            HttpStatus status = HttpStatus.UNAUTHORIZED; //401
            throw new BaseException(code, message, status);
        }
        return new Result("200", "成功", "业务3",true);
    }

}
