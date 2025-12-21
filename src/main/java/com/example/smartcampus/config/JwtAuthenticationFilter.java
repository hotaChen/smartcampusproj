package com.example.smartcampus.config;

import com.example.smartcampus.security.CustomUserDetailsService;
import com.example.smartcampus.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;



    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // 只拦截 API
        return !path.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        //  放行静态资源 & 登录
        if (
                uri.startsWith("/login.html") ||
                        uri.endsWith(".html") ||
                        uri.endsWith(".css") ||
                        uri.endsWith(".js") ||
                        uri.endsWith(".png") ||
                        uri.endsWith(".jpg") ||
                        uri.endsWith(".gif") ||
                        uri.startsWith("/api/auth/")
        ) {
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("=== JWT过滤器开始 ===");
        System.out.println("请求URL: " + request.getRequestURL());
        System.out.println("Authorization头: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("提取的JWT: " + jwt);

            try {
                username = jwtUtil.getUsernameFromToken(jwt);
                System.out.println("从Token解析的用户名: " + username);
            } catch (Exception e) {
                System.out.println("❌ Token解析失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Authorization头格式不正确或缺失");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("开始认证用户: " + username);

            try {
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                System.out.println("✅ 用户详情加载成功: " + username);
                System.out.println("用户权限: " + userDetails.getAuthorities());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    System.out.println("✅ Token验证成功");

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("✅ 认证上下文设置成功");
                    System.out.println("当前认证用户: " + SecurityContextHolder.getContext().getAuthentication().getName());
                } else {
                    System.out.println("❌ Token验证失败");
                }
            } catch (Exception e) {
                System.out.println("❌ 用户认证失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            if (username == null) {
                System.out.println("⚠️ 用户名为空，跳过认证");
            } else {
                System.out.println("⚠️ 已存在认证上下文，跳过设置");
            }
        }

        System.out.println("=== JWT过滤器结束 ===");
        chain.doFilter(request, response);
    }
}