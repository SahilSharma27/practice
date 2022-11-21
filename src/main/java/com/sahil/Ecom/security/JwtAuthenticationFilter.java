package com.sahil.Ecom.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.Ecom.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MyCustomUserDetailsService userDetailsService;

//    private Gson gson = new Gson();

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

//                ApiError apiError = new ApiError();
//                String apiErrorJsonString = this.gson.toJson(apiError);
//
//                PrintWriter out = response.getWriter();
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                out.print(apiErrorJsonString);
//                out.flush();


                Map<String, Object> errorDetails = new HashMap<>();

                errorDetails.put("message", "Invalid token");

                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(),errorDetails);


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
