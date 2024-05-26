package com.optimiza.clickbarber.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilter() {
        var registrationBean = new FilterRegistrationBean<JwtRequestFilter>();
        registrationBean.setFilter(jwtRequestFilter);

        var listUrlPatterns = List.of(
                "/barbearias/*",
                "/servicos/*");
        var collectionUrlPatterns = new ArrayList<>(listUrlPatterns);
        registrationBean.setUrlPatterns(collectionUrlPatterns);

        return registrationBean;
    }

}
