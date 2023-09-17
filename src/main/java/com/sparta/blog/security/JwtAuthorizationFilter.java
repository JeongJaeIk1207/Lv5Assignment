package com.sparta.blog.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blog.exception.RestApiException;
import com.sparta.blog.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public boolean isEmpty(String str) {
        if(str == null) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if ("/api/auth/login".equals(req.getRequestURI()) ||
                "/api/auth/signup".equals(req.getRequestURI()) ||
                req.getRequestURI().startsWith( "/v3/") ||
                req.getRequestURI().startsWith("/swagger-ui")) {
// 토큰이 비어 있을 때 예외 처리를 하지 않도록 조건문 추가
            filterChain.doFilter(req, res);
            return;
        }

        if(isEmpty(tokenValue)) {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setStatus(403);
            RestApiException restApiException = new RestApiException(403, "토큰이 유효하지 않습니다.");
            res.getWriter().write(new ObjectMapper().writeValueAsString(restApiException));
            return;
        }

        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.setStatus(403);
                RestApiException restApiException = new RestApiException(403, "토큰이 유효하지 않습니다.");
                res.getWriter().write(new ObjectMapper().writeValueAsString(restApiException));
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
