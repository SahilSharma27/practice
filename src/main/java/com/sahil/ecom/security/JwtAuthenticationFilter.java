package com.sahil.ecom.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.ecom.exception.TokenExpiredException;
import com.sahil.ecom.repository.BlacklistTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MyCustomUserDetailsService userDetailsService;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

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
            jwtToken = requestTokenHeader.substring(7);

            logger.info(jwtToken);
            try{
                //check if token is blacklisted
                if(blacklistTokenRepository.existsByAccessToken(jwtToken)){
                    logger.info("------------------BL-----------------");
                    logger.info("BLACK LISTED TOKEN");

                    throw new ExpiredJwtException(null,null,"in BL");
                }
                logger.info("-------------NBL----------------------");
                logger.info("NOT BLACK LISTED TOKEN");

                username = this.jwtUtil.extractUsername(jwtToken);

            }
            catch (ExpiredJwtException |MalformedJwtException e) {

//                Map<String, Object> errorDetails = new HashMap<>();
//
//                errorDetails.put("message", "Invalid token");
//
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.writeValue(response.getWriter(),errorDetails);


                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                final Map<String, Object> body = new HashMap<>();
                body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                body.put("error", "Unauthorized");
                body.put("message","TOKEN INVALID");
                body.put("path", request.getServletPath());

                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), body);


                throw new TokenExpiredException("FILTER:TOKEN EXPIRED");
            }catch (UsernameNotFoundException e){

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


    private boolean isBlackListed(String jwtToken) {
        logger.info(jwtToken);
        return blacklistTokenRepository.existsByAccessToken(jwtToken);

    }


}
