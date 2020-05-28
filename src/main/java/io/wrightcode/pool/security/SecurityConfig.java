package io.wrightcode.pool.security;

import java.util.logging.Logger;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.wrightcode.pool.controller.MonitorController;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	Logger log = Logger.getLogger(MonitorController.class.getName());
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/v1/api/status","/v2/api-docs", 
                "/swagger-resources/**",  
                "/swagger-ui.html", 
                "/webjars/**", "/v1/api/recent").permitAll()
                .requestMatchers(examplesRequestMatcher()).permitAll()
                .and().authorizeRequests()
                .requestMatchers((request) -> request.getServletPath().equals("/")).permitAll()
                .and().authorizeRequests()
                .requestMatchers(endpointRequestMatcher()).permitAll()
                .and().authorizeRequests()
                .anyRequest().denyAll();
        http.csrf().disable();
    }

    private RequestMatcher endpointRequestMatcher() {
        return EndpointRequest.to("metrics", "prometheus");
    }

    private RequestMatcher examplesRequestMatcher() {
        return (request) -> request.getServletPath().equals("/v1/api/status") ||
        		request.getServletPath().equals("/actuator/**") ||
        		request.getServletPath().equals("/v2/**");
    }
}
