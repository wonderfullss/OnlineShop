package com.example.onlineshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .requestMatchers(HttpMethod.GET, "/user/myNotifications").hasAuthority("USER")
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