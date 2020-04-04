package com.example.im.result;

import lombok.Data;

/**
 * @author HuJun
 * @date 2020/3/23 3:20 下午
 */
@Data
public class FriendResult {
    private String friendId;
    private boolean isBlacklisted;
    private String remark;
    private int labelId;
}
