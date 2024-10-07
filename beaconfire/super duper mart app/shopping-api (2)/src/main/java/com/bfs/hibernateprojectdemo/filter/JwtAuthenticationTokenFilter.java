package com.bfs.hibernateprojectdemo.filter;

import com.bfs.hibernateprojectdemo.config.JwtSecurityProperties;
import com.bfs.hibernateprojectdemo.utils.JwtTokenUtils;
import com.bfs.hibernateprojectdemo.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: git-dev
 * @Package: com.lq.pys.base.filter
 * @ClassName: JwtAuthenticationTokenFilter
 * @Author: xxx
 * @Description: JWT过滤器
 * @Date: 2021/2/18 11:07 上午
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    public JwtAuthenticationTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        JwtSecurityProperties jwtSecurityProperties = SpringContextHolder.getBean(JwtSecurityProperties.class);
        String requestRri = httpServletRequest.getRequestURI();
        //获取request token
        String token = null;
        String bearerToken = httpServletRequest.getHeader(jwtSecurityProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtSecurityProperties.getTokenStartWith())) {
            token = bearerToken.substring(jwtSecurityProperties.getTokenStartWith().length());
        }
// eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicGVybWlzc2lvbnMiOlt7ImF1dGhvcml0eSI6InVzZXJfd3JpdGUifSx7ImF1dGhvcml0eSI6InVzZXJfdXBkYXRlIn0seyJhdXRob3JpdHkiOiJ1c2VyX3JlYWQifSx7ImF1dGhvcml0eSI6InVzZXJfZGVsZXRlIn1dLCJpZCI6M30.hYUZVJ0UPyboxsi887YR0IyEpNRzXQv9EQu_ADahfHA
// eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNokjEEKwyAQAP-yZwW1G7f6m5XsUgtJQzS0EPL3WnqbmcOcwEd_QD5h49ber32GDD7cwEAdiAaOJvvKi4zO81JXuAw8ex2aqIgkVavRq0UNZO9OivUxRSZCnYr7fbhD9hRoShgxGJDP9g_RJYfh-gIAAP__.zCQDvkPNK7E8n7Tv2Q17Jnfe34G7nL8QfsnE5A2WnYNWO-6xZ-p7AbNwpK_hJQLCXgXJ_LimZf6Vp-nO0_XMKQ
// eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9
        //
        // eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9
        if (StringUtils.hasText(token) && jwtTokenUtils.validateToken(token)) {
            Authentication authentication = jwtTokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);
        } else {
            log.debug("no valid JWT token found, uri: {}", requestRri);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
