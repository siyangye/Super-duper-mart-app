package com.bfs.hibernateprojectdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: git-dev
 * @Package: com.lq.pys.base.config
 * @ClassName: JwtSecurityProperties
 * @Author: xxx
 * @Description: JWT配置类
 * @Date: 2021/2/18 10:55 上午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {

    /** Request Headers ： Authorization */
    private String header;

    /** 令牌前缀，最后留个空格 Bearer */
    private String tokenStartWith;

    /** Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位/毫秒 */
    private Long tokenValidityInSeconds = 14400000L;

    /**返回令牌前缀 */
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }

}
