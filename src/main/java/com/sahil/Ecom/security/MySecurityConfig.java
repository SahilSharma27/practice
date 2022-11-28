package com.sahil.Ecom.security;

import com.sahil.Ecom.enums.EcomRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyCustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/token/refresh").permitAll()
                .antMatchers("/welcome").permitAll()
                .antMatchers("/upload").permitAll()
                .antMatchers("/images/users/**").permitAll()
                .antMatchers(HttpMethod.GET,"/users/activate").permitAll()
                .antMatchers("/users/forgotPassword").permitAll()
                .antMatchers("/users/resetPassword/**").permitAll()
                .antMatchers("/users/image/**").permitAll()
                .antMatchers("/users/customersPaged").permitAll()
                .antMatchers("/users/sellersPaged").permitAll()
                .antMatchers("/users/update/profile").hasAnyRole(EcomRoles.ADMIN.label,EcomRoles.CUSTOMER.label,EcomRoles.SELLER.label)
                .antMatchers("/users/update/password").hasAnyRole(EcomRoles.ADMIN.label,EcomRoles.CUSTOMER.label,EcomRoles.SELLER.label)
                .antMatchers("/users/profile").hasAnyRole(EcomRoles.ADMIN.label,EcomRoles.CUSTOMER.label,EcomRoles.SELLER.label)
                .antMatchers("/users/logout").hasAnyRole(EcomRoles.ADMIN.label,EcomRoles.CUSTOMER.label,EcomRoles.SELLER.label)
                .antMatchers("/users/address").hasRole(EcomRoles.CUSTOMER.label)
                .antMatchers("/users").hasRole(EcomRoles.ADMIN.label)
                .antMatchers("/users/customers").hasRole(EcomRoles.ADMIN.label)
                .antMatchers("/users/sellers").hasRole(EcomRoles.ADMIN.label)
                .antMatchers("/users/seller/**").hasRole(EcomRoles.ADMIN.label)
                .antMatchers(HttpMethod.PUT,"/users/activate").hasRole(EcomRoles.ADMIN.label)
                .antMatchers(HttpMethod.PUT,"/users/deactivate").hasRole(EcomRoles.ADMIN.label)
                .antMatchers("/category/metadata").hasRole(EcomRoles.ADMIN.label)
                .antMatchers("/category").hasRole(EcomRoles.ADMIN.label)
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
