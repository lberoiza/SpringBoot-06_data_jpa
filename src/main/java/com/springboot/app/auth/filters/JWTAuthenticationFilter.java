package com.springboot.app.auth.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;


  public JWTAuthenticationFilter(AuthenticationManager authenticationManager1) {
    this.authenticationManager = authenticationManager1;
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String username = this.obtainUsername(request);
    username = username != null ? username.trim() : "";
    String password = this.obtainPassword(request);
    password = password != null ? password : "";

    logger.info("JWT username :" + username);
    logger.info("JWT password :" + password);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);


    return this.authenticationManager.authenticate(authToken);
  }
}
