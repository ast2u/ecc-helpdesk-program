package com.carloprogram.security.config;

import com.carloprogram.service.impl.EmployeeUserDetailsServiceImpl;
import com.carloprogram.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final ApplicationContext context;

    public JwtFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();

        // If the request is for /refresh, check if there's a refresh token in cookies
        if ("/refresh".equals(requestPath)) {
            String refreshToken = getRefreshTokenFromCookie(request);
            if (refreshToken == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is missing");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        } //

        if(username != null && SecurityContextHolder
                .getContext()
                .getAuthentication() == null ){ //

            UserDetails userDetails = context.getBean(EmployeeUserDetailsServiceImpl.class)
                    .loadUserByUsername(username); //

            if(jwtService.validateToken(token, userDetails)){ //
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request)); //
                SecurityContextHolder.getContext().setAuthentication(authToken); //
            }
        }
        filterChain.doFilter(request, response); //
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
