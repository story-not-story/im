package com.example.im.DTO;

import lombok.Data;

/**
 * @author HuJun
 * @date 2020/4/3 6:43 下午
 */
@Data
public class MessageDTO {
    private String id;
    private String content;
    private Byte status;
}
