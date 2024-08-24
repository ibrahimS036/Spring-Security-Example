package com.security.config;

import jakarta.servlet.FilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableMethodSecurity  Used this annotation when you want to used method based authentication.
public class Config24 {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails normal = User.
                withUsername("ibbo").
                password(passwordEncoder().encode("ibbo")).
                roles("NORMAL").
                build();

        UserDetails admin = User.
                withUsername("leo").
                password(passwordEncoder().encode("leo")).
                roles("ADMIN").
                build();

        return new InMemoryUserDetailsManager(normal, admin);
        //if you used method level authentication then you need to create a CustomUserDetailService and implement this class to UserDetailService.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeHttpRequests()

                //Admin Authorization
                .requestMatchers("/home/admin")
                .hasRole("ADMIN")

                // Normal User Authorization
                .requestMatchers("/home/normal")
                .hasRole("NORMAL")

                // Public Access To All
                .requestMatchers("/home/public")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

        return httpSecurity.build();

    }
}
