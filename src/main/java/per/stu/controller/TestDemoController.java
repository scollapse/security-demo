package per.stu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.stu.model.Result;

/**
 * 测试控制类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/17 14:19
 * @modified by
 */
@RestController
@RequestMapping("/open-api")
public class TestDemoController {



    @GetMapping("/business-1")
    public Result getA(){
        return new Result("200", "成功", "业务1");
    }

    @GetMapping("/business-2")
    public Result getB(){
        if (true) {
            throw new RuntimeException("业务2异常");
        }
        return new Result("200", "成功", "业务2");
    }

}
