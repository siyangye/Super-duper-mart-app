package com.bfs.hibernateprojectdemo.out;


import com.bfs.hibernateprojectdemo.domain.User;
import lombok.Data;

import java.io.Serializable;
@Data
public class LoginAdminUserOut implements Serializable {
    private User user; // 用户信息
    private String token; // JWT令牌

}
