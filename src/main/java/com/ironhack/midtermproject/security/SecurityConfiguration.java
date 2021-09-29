package com.ironhack.midtermproject.security;

import com.ironhack.midtermproject.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) // Override the protected methods to provide custom implementation
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests()
                .mvcMatchers("/accounts").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/accounts/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/checking/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/checking").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/student/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/student").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/savings").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/savings/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/credit").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/credit/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/user").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/user/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/accountHolder").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/accountHolder/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/thirdParty").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/thirdParty/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/myAccount").hasAuthority("ROLE_ACCOUNTHOLDER")
                .mvcMatchers("/transaction").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/transaction/**").hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/newTransaction").hasAuthority("ROLE_ACCOUNTHOLDER")
                .mvcMatchers("/TP/transaction/**").permitAll()
                .anyRequest().permitAll();
        http.csrf().disable();
    }

}
