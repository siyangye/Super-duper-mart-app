package com.bfs.hibernateprojectdemo.jwt;

import com.bfs.hibernateprojectdemo.core.BDic;
import com.bfs.hibernateprojectdemo.core.BaseOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: git-dev
 * @Package: com.lq.pys.base.common
 * @ClassName: JwtAuthenticationEntryPoint
 * @Author: xxx
 * @Description: JWT认证失败处理类
 * @Date: 2021/2/18 11:31 上午
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        BaseOut baseOut = new BaseOut();
        baseOut.setCode(BDic.FAIL);
        baseOut.setMessage("You do not have permission to view this page, please contact the administrator.");
        baseOut.setTimestamp(Long.valueOf(System.currentTimeMillis()).toString());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), baseOut);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}

