package com.tutomato.userservice.common.filter;

import com.tutomato.userservice.common.AuthorizationHeaderHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationHeaderCaptureFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header != null) {
                AuthorizationHeaderHolder.set(header);
            }
            filterChain.doFilter(request, response);
        } finally {
            AuthorizationHeaderHolder.clear();
        }
    }

}
