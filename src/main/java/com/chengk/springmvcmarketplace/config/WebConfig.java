package com.chengk.springmvcmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chengk.springmvcmarketplace.domain.CategoryDtoFormatter;
import com.chengk.springmvcmarketplace.domain.UserDtoFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserDetailsService userDetailsService;

    public WebConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatter(categoryDtoFormatter());
        registry.addFormatter(userDtoFormatter());
    }

    @Bean
    public CategoryDtoFormatter categoryDtoFormatter() {
        return new CategoryDtoFormatter();
    }

    @Bean
    public UserDtoFormatter userDtoFormatter() {
        return new UserDtoFormatter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((security) -> {
                    security.requestMatchers("/login", "/register", "/styles/**", "/js/**", "/images/**")
                            .permitAll()
                            .requestMatchers("/categories")
                            .hasAuthority("SUPER_USER")
                            .anyRequest().hasAnyAuthority("USER", "SUPER_USER");
                })
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/products"))
                .logout(logout -> logout.logoutSuccessUrl("/login"))
                .build();
    }
}
