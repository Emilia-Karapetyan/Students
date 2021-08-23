package com.example.demo.student.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/user/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/user/admin").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/user/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/publisher/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/publisher/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/v1/publisher/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/author/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/author/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/v1/author/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/genre/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/genre/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/v1/genre/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/book/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/refresh-token/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
