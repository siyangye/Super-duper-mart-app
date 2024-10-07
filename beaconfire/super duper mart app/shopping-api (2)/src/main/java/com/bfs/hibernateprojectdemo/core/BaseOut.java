package com.bfs.hibernateprojectdemo.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseOut<T> {
    // 状态码
    private Integer code;
    // 消息
    private String message;
    // 数据
    private T data;
    // 时间戳
    private String timestamp;
}
