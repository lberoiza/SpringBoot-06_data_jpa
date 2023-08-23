package com.springboot.app.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

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

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    User user = (User) authResult.getPrincipal();

    String token = Jwts.builder()
        .setSubject(user.getUsername())
        .signWith(SECRET_KEY)
        .compact();

    response.addHeader("Authorization", String.format("Bearer %s", token));

    Map<String, Object> body = new HashMap<>();
    body.put("token", token);
    body.put("user", user);
    body.put("message", String.format("Hi, %s. You has initialized session successfully", user.getUsername()));

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(200);
    response.setContentType("application/json");

  }
}
