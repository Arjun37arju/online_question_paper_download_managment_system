package com.online_question_paper.online_question_paper_download_system.config;

import com.online_question_paper.online_question_paper_download_system.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                // Disable CSRF
                .csrf(csrf -> csrf.disable())

                // Authorization
                .authorizeHttpRequests(auth -> auth

                        // ✅ PUBLIC PAGES (NO LOGIN REQUIRED)
                        .requestMatchers(
                                "/admin/login",

                                // Principal pages handled by session controller
                                "/principal/**",

                                // Static files
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // ✅ ADMIN MODULE (LOGIN REQUIRED)
                        .requestMatchers("/admin/**").authenticated()

                        // ✅ OTHER REQUESTS ALLOWED
                        .anyRequest().permitAll()
                )

                // ✅ ADMIN LOGIN
                .formLogin(form -> form

                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")

                        .defaultSuccessUrl("/admin/dashboard", true)

                        .failureUrl("/admin/login?error=true")

                        .permitAll()
                )

                // Database authentication
                .userDetailsService(userDetailsService)

                // Logout
                .logout(logout -> logout

                        .logoutUrl("/admin/logout")

                        .logoutSuccessUrl("/admin/login")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .clearAuthentication(true)
                );

        return http.build();
    }
}