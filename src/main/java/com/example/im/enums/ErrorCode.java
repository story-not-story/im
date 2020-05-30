package com.example.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: HuJun
 * @date: 2020/3/21 1:19 下午
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 接口参数不符
     */
    PARAM_ERROR(1, "参数错误"),
    /**
     * JSON格式错误
     */
    JSON_ERROR(2, "不符合json格式"),
    /**
     * 使得用户数据访问隔离
     */
    OWNER_ERROR(3, "该用户不是当前用户"),
    /**
     *  登录时报错用户未注册
     */
    USER_NOT_EXISTS(4, "用户未注册"),
    /**
     * 注册时报错用户已存在
     */
    USER_ALREADY_EXISTS(5, "用户已存在"),
    /**
     * 登出时报错用户未登录
     */
    USER_NOT_LOGIN(6, "用户未登录"),
    /**
     * 登录时报错用户已登录
     */
    USER_ALREADY_LOGIN(7, "用户已登录"),
    /**
     * 添加好友时报错该好友已存在
     */
    FRIEND_ALREADY_EXISTS(8, "该好友已存在"),
    /**
     * 删除好友时报错该好友不存在
     */
    FRIEND_NOT_EXISTS(9, "该好友不存在"),
    /**
     * 拉黑好友时报错该好友已拉黑
     */
    FRIEND_ALREADY_BLACKLISTED(10, "该好友已拉黑"),
    /**
     * 解除拉黑好友时报错该好友已解除拉黑
     */
    FRIEND_ALREADY_UNBLACKLISTED(11, "该好友已解除拉黑"),
    /**
     * 该群不存在
     */
    GROUP_NOT_EXISTS(12, "该群不存在"),
    /**
     * 该分组不存在
     */
    LABEL_NOT_EXISTS(13, "该分组不存在"),
    /**
     * 分组必须不同
     */
    LABEL_MUST_DIFF(14, "分组必须不同"),
    /**
     * 邀请（申请）不存在或者已过期
     */
    INVITATION_NOT_FOUND(15, "邀请（申请）不存在或者已过期"),
    /**
     * 已处理邀请（申请）
     */
    INVITATION_ALREADY_HANDLE(16, "已处理邀请（申请）"),
    /**
     * 修改前后好友备注不能相同
     */
    REMARK_NOT_SAME(17, "修改前后好友备注不能相同"),
    /**
     * 该群成员不存在
     */
    MEMBER_NOT_EXISTS(18, "该群成员不存在"),
    /**
     * 该群成员已存在
     */
    MEMBER_ALREADY_EXISTS(18, "该群成员已存在"),
    /**
     * 该消息记录不存在
     */
    MESSAGE_NOT_EXISTS(19, "该消息记录不存在"),
    /**
     * websocket发生错误
     */
    WEBSOCKET_ERROR(20, "websocket发生错误，等待自动重连"),
    /**
     * 对方离线，请刷新页面
     */
    REFRESH(21, "对方离线，请刷新页面"),
    /**
     * 用户密码错误
     */
    PASSWORD_ERROR(22, "用户密码错误"),
    /**
     * 没有图片
     */
    IMG_NOT_EXISTS(23, "无图");
    private Integer code;
    private String msg;

}
