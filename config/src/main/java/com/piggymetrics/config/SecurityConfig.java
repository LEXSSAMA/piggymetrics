package com.piggymetrics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author cdov
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();  //关闭csrf防护机制
        http
            .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()  //url为/actuator/**的http访问无需认证
                .anyRequest().authenticated()
            .and()
                .httpBasic()
                ;
    }
}
