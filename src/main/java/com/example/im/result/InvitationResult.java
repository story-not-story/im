package com.example.im.result;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/4/16 11:30 下午
 */
@Data
public class InvitationResult {
    private String id;
    private String senderId;
    private String receiverId;
    private Integer labelId;
    private String message;
    private Boolean isAccepted;
    private String remark;
    private String avatar;
}
