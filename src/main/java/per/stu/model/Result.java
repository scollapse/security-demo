package per.stu.model;

/**
 * 统一结果类
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/17 14:17
 * @modified by
 */
public class Result {
    private String code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
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
