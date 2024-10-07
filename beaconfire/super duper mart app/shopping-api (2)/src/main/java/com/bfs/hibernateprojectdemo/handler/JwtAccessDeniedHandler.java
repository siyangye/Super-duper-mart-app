package com.bfs.hibernateprojectdemo.handler;

import com.bfs.hibernateprojectdemo.core.BDic;
import com.bfs.hibernateprojectdemo.core.BaseOut;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: git-dev
 * @Package: com.lq.pys.base.common
 * @ClassName: JwtAccessDeniedHandler
 * @Author: xxx
 * @Description: jwt无权限访问类
 * @Date: 2021/2/18 11:28 上午
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
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

