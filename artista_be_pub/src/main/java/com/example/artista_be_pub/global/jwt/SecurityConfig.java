package com.artista.main.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/aa/auth/login").permitAll()
//                .antMatchers("/aa/auth/join").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**")
//                .and().headers().frameOptions().disable()
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManagerBuilder), UsernamePasswordAuthenticationFilter.class)
                ;
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/h2-console/**").permitAll()
                                .antMatchers("/aa/auth/login").permitAll()
                                .antMatchers("/aa/auth/join").permitAll()
                                .antMatchers("/user/findId").permitAll()
                                .antMatchers("/user/restPw").permitAll()
                                .antMatchers("/user/idCheck").permitAll()
                                .antMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers ->
                        headers.frameOptions(frameOptions ->
                                frameOptions.disable() // Disable X-Frame-Options for H2 console
                        )
                )
                .csrf(csrf ->
                        csrf.ignoringAntMatchers("/h2-console/**")
                                .disable()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManagerBuilder), UsernamePasswordAuthenticationFilter.class);
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
