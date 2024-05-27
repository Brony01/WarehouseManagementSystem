package com.java.warehousemanagementsystem.config.filter;


import com.java.warehousemanagementsystem.config.SecurityUserDetails;
import com.java.warehousemanagementsystem.service.SecurityUserDetailsService;
import com.java.warehousemanagementsystem.utils.JwtUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;


@Component
public class MyAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestToken = request.getHeader(JwtUtils.getCurrentConfig().getHeader());

        if (requestToken != null && !requestToken.trim().isEmpty()) {
            boolean verifyToken = JwtUtils.isValidToken(requestToken);

            if (!verifyToken) {
                filterChain.doFilter(request, response);
                return;
            }

            String subject = JwtUtils.getSubject(requestToken);

            if (subject != null && !subject.trim().isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                Mono<SecurityUserDetails> userDetailsMono = securityUserDetailsService.findByUsername(subject)
                        .cast(SecurityUserDetails.class);

                userDetailsMono.subscribeOn(Schedulers.boundedElastic())
                        .doOnNext(userDetails -> {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        })
                        .doOnTerminate(() -> {
                            try {
                                filterChain.doFilter(request, response);
                            } catch (IOException | ServletException e) {
                                e.printStackTrace();
                            }
                        })
                        .subscribe();
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
