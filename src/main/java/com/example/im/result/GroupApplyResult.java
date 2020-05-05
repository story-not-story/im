package com.example.im.result;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author HuJun
 * @date 2020/5/4 7:43 下午
 */
@Data
public class GroupApplyResult {
    private String id;
    private String userId;
    private String groupId;
    private String message;
    private Boolean isAccepted;
    private String avatar;
}
