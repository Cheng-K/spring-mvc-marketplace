package com.chengk.springmvcmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chengk.springmvcmarketplace.domain.CategoryDtoFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserDetailsService userDetailsService;

    public WebConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatter(categoryDtoFormatter());
    }

    @Bean
    public CategoryDtoFormatter categoryDtoFormatter() {
        return new CategoryDtoFormatter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((security) -> {
                    security.requestMatchers("/login", "/register", "/styles/**", "/js/**")
                            .permitAll()
                            .anyRequest().hasAnyAuthority("USER");
                })
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/products"))
                .build();
    }
}
