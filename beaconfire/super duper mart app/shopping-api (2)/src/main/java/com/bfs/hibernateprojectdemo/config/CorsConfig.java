package com.bfs.hibernateprojectdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @ProjectName: git-dev
 * @Package: com.lq.pys.base.config
 * @ClassName: CorsConfig
 * @Author: xxx
 * @Description: 解决跨域问题
 * @Date: 2021/2/18 1:39 下午
 */
@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        /**
         * 你需要跨域的地址  注意这里的 127.0.0.1 != localhost
         * 表示只允许http://localhost:8080地址的访问（重点哦！！！！）
         * corsConfiguration.addAllowedOrigin("http://localhost:8080");
         */
        //允许所有域名进行跨域调用
//        corsConfiguration.addAllowedOrigin("*");
        //放行全部原始头信息
        corsConfiguration.addAllowedHeader("*");
        //允许所有请求方法跨域调用
        corsConfiguration.addAllowedMethod("*");
        //允许跨越发送cookie
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200", "http://localhost:4201"));
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //配置 可以访问的地址
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
}
