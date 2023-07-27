package com.springboot.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

  public static final String[] ENDPOINTS_WHITELIST = {
      "/",
      "/css/**",
      "/js/**",
      "/images/**",
      "/client/list"
  };

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() throws Exception {

    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    manager.createUser(User.withUsername("user")
        .password(passwordEncoder().encode("user"))
        .roles("USER").build());

    manager.createUser(User.withUsername("admin")
        .password(passwordEncoder().encode("admin"))
        .roles("ADMIN", "USER").build());

    return manager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request ->
            request
                .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                .requestMatchers("/client/{id}").hasAnyRole("USER")
                .requestMatchers("/uploads/**").hasAnyRole("USER")
                .requestMatchers("/client/**").hasAnyRole("ADMIN")
                .requestMatchers("/invoice/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
        )
        .logout(LogoutConfigurer::permitAll);
    return http.build();
  }

}