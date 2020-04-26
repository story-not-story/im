package com.example.im.result;

import lombok.Data;

/**
 * @author HuJun
 * @date 2020/4/17 9:27 下午
 */
@Data
public class MemberResult {
    private String id;
    private String groupId;
    private String userId;
    private Byte grade = 0;
    private String name;
    private String avatar;
}
