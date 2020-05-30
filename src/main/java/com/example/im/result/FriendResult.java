package com.example.im.result;

import lombok.Data;

/**
 * @author HuJun
 * @date 2020/3/23 3:20 下午
 */
@Data
public class FriendResult {
    private String friendId;
    private Boolean isFriendBlacklisted;
    private Boolean isMeBlacklisted;
    private String remark;
    private Integer labelId;
    private String avatar;
    private String signature;
    private String name;
}
