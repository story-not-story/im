package com.example.im.result;

import lombok.Data;

import java.util.List;

/**
 * @author HuJun
 * @date 2020/5/22 3:53 下午
 */
@Data
public class MessageGroupResult {
    private String id;
    private String avatar;
    private String name;
    private List<MessageResult> msglist;
}
