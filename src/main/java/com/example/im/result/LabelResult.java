package com.example.im.result;

import lombok.Data;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/3/23 3:09 下午
 */
@Data
public class LabelResult {
    private Integer id;
    private String name;
    private String userId;
    private List<FriendResult> friendList;
}
