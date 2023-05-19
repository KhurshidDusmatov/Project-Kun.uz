package com.example.config;

import com.example.dto.jwt.JwtDTO;
import com.example.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return Arrays.asList(SecurityConfig.AUTH_WHITELIST).stream()
                .anyMatch(p -> {
                    boolean match = pathMatcher.match(p, request.getServletPath());
                    return match;
                });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("doFilter method");
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        boolean matched = Arrays.asList(SecurityConfig.AUTH_WHITELIST).stream()
//                .anyMatch(p -> {
//                    boolean match = pathMatcher.match(p, request.getServletPath());
//                    return match;
//                });
//        if (matched){
//            filterChain.doFilter(request, response);
//            return;
//        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token not found");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        JwtDTO jwtDTO;
        try {
            jwtDTO = JwtUtil.decode(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtDTO.getEmail());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Message", "Token not found");
            return;
        }
    }


    //    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        final HttpServletRequest request = (HttpServletRequest) servletRequest;
//        final HttpServletResponse response = (HttpServletResponse) servletResponse;
//        final String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setHeader("Message", "Token not found");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authHeader.substring(7);
//        JwtDTO jwtDTO;
//        try {
//            jwtDTO = JwtUtil.decode(token);
//        }catch (JwtException e){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setHeader("Message", "Token not found");
//            return;
//        }
//        request.setAttribute("id", jwtDTO.getEmail());
//        request.setAttribute("role", jwtDTO.getRole());
//        filterChain.doFilter(request, response);
//    }
}
