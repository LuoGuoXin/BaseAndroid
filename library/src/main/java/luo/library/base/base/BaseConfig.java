package luo.library.base.base;

/**
 * 信息配置
 */

public class BaseConfig {
    /**
     * 标题栏背景颜色
     */
    private String titleViewBackgroundColor = "#9EEA6A";
    /**
     * 网络请求成功返回的code码
     */
    private int code = 1;
    /**
     * 网络请求返回的code
     */
    private String httpCode = "code";
    /**
     * 网络请求返回的message
     */
    private String httpMessage = "message";
    /**
     * 网络请求返回的result
     */
    private String httpResult = "result";

    public String getTitleViewBackgroundColor() {
        return titleViewBackgroundColor;
    }

    public BaseConfig setTitleViewBackgroundColor(String titleViewBackgroundColor) {
        this.titleViewBackgroundColor = titleViewBackgroundColor;
        return this;
    }

    public int getCode() {
        return code;
    }

    public BaseConfig setCode(int code) {
        this.code = code;
        return this;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public BaseConfig setHttpCode(String httpCode) {
        this.httpCode = httpCode;
        return this;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public BaseConfig setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
        return this;
    }

    public String getHttpResult() {
        return httpResult;
    }

    public BaseConfig setHttpResult(String httpResult) {
        this.httpResult = httpResult;
        return this;
    }
}
