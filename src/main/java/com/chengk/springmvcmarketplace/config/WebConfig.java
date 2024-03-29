package com.chengk.springmvcmarketplace.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chengk.springmvcmarketplace.domain.CategoryDtoFormatter;
import com.chengk.springmvcmarketplace.domain.UserDtoFormatter;
import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;

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
                    security.requestMatchers("/login", "/register", "/styles/**", "/js/**", "/images/**",
                            "/staticImages/**",
                            "/reset-password", "/change-password", "/error")
                            .permitAll()
                            .requestMatchers("/categories")
                            .hasAuthority("SUPER_USER")
                            .anyRequest().hasAnyAuthority("USER", "SUPER_USER");
                })
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/"))
                .logout(logout -> {
                    logout.logoutSuccessUrl("/login");
                    logout.invalidateHttpSession(false);
                    logout.deleteCookies("JSESSIONID");
                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.invalidSessionUrl("/login?sessionExpired");
                })
                .build();
    }

    @Bean
    public KeyPair rsaKeyPair() {
        KeyPairGenerator keypairGenerator;
        try {
            keypairGenerator = KeyPairGenerator.getInstance("rsa");
            keypairGenerator.initialize(2048);
            return keypairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.", "Please try again or contact support."));
        }
    }
}
