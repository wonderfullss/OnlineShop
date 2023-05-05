package com.example.onlineshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/shop/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/shop/signin").permitAll()
                .requestMatchers(HttpMethod.PUT, "/product/review").hasAuthority("USER")
                .requestMatchers(HttpMethod.PUT, "/user/return/product").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET, "/my/history").hasAuthority("USER")
                .requestMatchers(HttpMethod.PUT, "/user/buy").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/organization/product/add").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/register/organization").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET, "/user/myNotifications").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/admin/validate/organization/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/delete/organization/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/access/organization/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/access/user/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/delete/user/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/admin/user/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/admin/orderList/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/alert").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/balance").hasAuthority("ADMIN")
                .requestMatchers("/actuator/shutdown").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}