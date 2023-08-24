package com.springboot.app.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JWTService jwtService;

  private final AuthenticationManager authenticationManager;

  private final ObjectMapper jsonParser;


  public JWTAuthenticationFilter(AuthenticationManager authenticationManager1, JWTService jwtService) {
    this.authenticationManager = authenticationManager1;
    this.jsonParser = new ObjectMapper();
    this.jwtService = jwtService;
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String username = this.obtainUsername(request);
    username = username != null ? username.trim() : "";
    String password = this.obtainPassword(request);
    password = password != null ? password : "";

    if (username.isBlank() && password.isBlank()) {
      try {
        com.springboot.app.models.entity.User user = this.jsonParser.readValue(request.getInputStream(), com.springboot.app.models.entity.User.class);
        username = user.getUsername();
        password = user.getPassword();
        logger.info("getting data from request");
      } catch (Exception e) {
        System.out.println(e.toString());
      }
    }

    logger.info("JWT username :" + username);
    logger.info("JWT password :" + password);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    return this.authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String token = this.jwtService.create(authResult);
    response.addHeader(JWTService.HEADER_AUTHORIZATION, JWTService.HEADER_TOKEN_PREFIX + token);

    Map<String, Object> body = new HashMap<>();
    body.put(JWTService.RESPONSE_KEY_TOKEN, token);
    body.put(JWTService.RESPONSE_KEY_USER, authResult.getName());
    body.put("message", String.format("Hi, %s. You has initialized session successfully", authResult.getName()));

    response.getWriter().write(this.jsonParser.writeValueAsString(body));
    response.setStatus(200);
    response.setContentType("application/json");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    Map<String, Object> body = new HashMap<>();
    body.put("message", "User or Password incorrect");
    body.put("error", failed.getMessage());

    response.getWriter().write(this.jsonParser.writeValueAsString(body));
    response.setStatus(401);
    response.setContentType("application/json");
  }
}
