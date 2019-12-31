package com.kmsoft.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你要找的问题不在了"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NOT_LOGIN(2003,"当前未登录，请登录"),
    SYS_ERROR(2004,"服务器冒烟了，要不稍后再试试?"),
    TYPE_PARAM_ERROR(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"啊哦，你要回复的评论已经不存在了");

    private Integer code;
    private String message;



    CustomizeErrorCode(Integer code , String message) {
        this.code = code;
        this.message = message;

    }
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

}
