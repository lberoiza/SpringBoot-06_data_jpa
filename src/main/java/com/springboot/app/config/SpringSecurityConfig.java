package com.springboot.app.config;

import com.springboot.app.auth.filters.JWTAuthenticationFilter;
import com.springboot.app.auth.handlers.LoginSuccessHandler;
import com.springboot.app.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {

  public static final String ERROR_403_URL = "/error_403";

  public static final String[] ENDPOINTS_WHITELIST = {
      "/",
      "/icons/**",
      "/css/**",
      "/js/**",
      "/images/**",
      "/client/list",
      "/locale"
  };

  private final LoginSuccessHandler loginSuccessHandler;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JpaUserDetailsService userDetailsService;

  private final AuthenticationConfiguration authenticationConfiguration;

  @Autowired
  public SpringSecurityConfig(
      LoginSuccessHandler loginSuccessHandler,
      BCryptPasswordEncoder passwordEncoder,
      JpaUserDetailsService userDetailsService,
      AuthenticationConfiguration authenticationConfiguration
  ) {
    this.loginSuccessHandler = loginSuccessHandler;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @Autowired
  public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
    build.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
  }


  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(request ->
            request
                .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
//                .requestMatchers("/client/{id}").hasAnyRole("USER")
//                .requestMatchers("/uploads/**").hasAnyRole("USER")
//                .requestMatchers("/client/**").hasAnyRole("ADMIN")
//                .requestMatchers("/invoice/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
        .addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(httpSecuritySessionManagementConfigurer ->
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    return http.build();
  }


  //  El inicio de sesión redirigirá por defecto al último recurso restringido solicitado de la sesión HTTP actual.
  //  Aparentemente también ha cubierto los recursos JS/CSS/imagen de las páginas HTML generadas.
  //  Cuando la propia página de inicio de sesión hace referencia exactamente a ese archivo JavaScript,
  //  entonces se recordaría como el último recurso restringido solicitado y Spring Security redirigiría ciegamente a él
  //  después de un inicio de sesión satisfactorio.
  //
  //  Es necesario indicar a Spring Security que los excluya de los recursos restringidos.
  //  Una forma sería añadir la siguiente línea al fichero de configuración XML de Spring Security.
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/application/js/**");
  }

}