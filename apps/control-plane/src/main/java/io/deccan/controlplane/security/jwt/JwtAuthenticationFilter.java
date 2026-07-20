package io.deccan.controlplane.security.jwt;

import io.deccan.controlplane.security.principal.AuthenticatedUser;
import io.deccan.controlplane.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;

        }

        final String token =
                authorizationHeader.substring(7);

        final String username =
                jwtService.extractUsername(token);

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtService.isValid(token, userDetails)) {

                Set<String> roles =
                        userDetails.getAuthorities()
                                .stream()
                                .map(a -> a.getAuthority())
                                .filter(a -> a.startsWith("ROLE_"))
                                .map(a -> a.substring(5))
                                .collect(Collectors.toSet());

                Set<String> permissions =
                        userDetails.getAuthorities()
                                .stream()
                                .map(a -> a.getAuthority())
                                .filter(a -> !a.startsWith("ROLE_"))
                                .collect(Collectors.toSet());

                AuthenticatedUser principal =
                        AuthenticatedUser.builder()
                                .userId(jwtService.extractUserId(token))
                                .organizationId(jwtService.extractOrganizationId(token))
                                .email(userDetails.getUsername())
                                .roles(roles)
                                .permissions(permissions)
                                .build();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            }

        }

        filterChain.doFilter(request, response);

    }

}