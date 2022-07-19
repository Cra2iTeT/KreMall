package com.kdfus.system;

/**
 * @author Cra2iTeT
 * @date 2022/5/31 22:43
 */
public enum ServiceResultEnum {
    ERROR("错误"),

    SUCCESS("成功"),

    EXISTED("已存在！"),

    OPERATE_ERROR("操作失败"),

    UPDATE_ERROR("修改失败"),

    DATE_NULL("数据空"),

    PASSWORD_DIFFERENT("两次密码不一致！"),

    PASSWORD_NULL("密码不允许为空"),

    PASSWORD_SAME("新旧密码不允许一致"),

    INFO_GET_ERROR("信息获取失败"),

    LOGIN_ACCOUNT_ID_VALID("请输入正确的手机号！"),

    LOGIN_ERROR("账号或密码错误！"),

    LOGOUT_ERROR("登出失败"),

    LOGIN_NULL("未登录"),

    LOGIN_FORM_NULL("账号、密码不允许为空！");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
