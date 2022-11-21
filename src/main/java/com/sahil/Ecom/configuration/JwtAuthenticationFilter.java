package com.sahil.Ecom.configuration;


import com.sahil.Ecom.exception.ApiError;
import com.sahil.Ecom.exception.TokenExpiredException;
import com.sahil.Ecom.helper.JwtUtil;
import com.sahil.Ecom.service.MyCustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MyCustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get jwt
        //bearer
        //validate

        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;


        //check format
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
            jwtToken = requestTokenHeader.substring("Bearer".length());

            try{
                username = this.jwtUtil.extractUsername(jwtToken);

            }catch (ExpiredJwtException e) {
               // e.printStackTrace();
                throw new TokenExpiredException("FILTER:TOKEN EXPIRED");
            }
            catch (UsernameNotFoundException e){

                throw new UsernameNotFoundException("FILTER:NO USER NAME FOUND");
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =

                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } else {
                System.out.println("Token is not validated...");
            }

        }

        filterChain.doFilter(request, response);

    }


}
