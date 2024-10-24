package per.stu.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 统一结果类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/17 14:17
 * @modified by
 */
@Data
@Builder
public class Result {
    private String code;
    private String message;
    private Object data;
    private boolean success;

    public Result() {
    }

    public Result(String code, String message, Object data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    /*
     * @description 失败默认返回500 无数据
     * @author syl
     * @date 2024/10/21 13:58
     * @param message
     * @return per.stu.model.vo.Result
     */
    public static Result fail(String message) {
        return new Result("internal.internal.server.error", message, "",false);
    }

    public static Result fail(String code,String message) {
        return new Result(code, message, "",false);
    }

    /*
     * @description 成功默认返回200
     * @author syl
     * @date 2024/10/21 13:58
     * @param message
    * @param data
     * @return per.stu.model.vo.Result
     */
    public static Result success(String message, Object data) {
        return new Result("success", message, data,true);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
