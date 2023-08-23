package com.springboot.app.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  private final AuthenticationManager authenticationManager;

  private final ObjectMapper jsonParser;


  public JWTAuthenticationFilter(AuthenticationManager authenticationManager1) {
    this.authenticationManager = authenticationManager1;
     this.jsonParser = new ObjectMapper();
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
    User user = (User) authResult.getPrincipal();

    Claims claims = Jwts.claims();
    claims.put("authorities", this.jsonParser.writeValueAsString(user.getAuthorities()));
    long milisecondPerHour = 3600000L;

    String token = Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .signWith(SECRET_KEY)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 4 * milisecondPerHour))
        .compact();

    response.addHeader("Authorization", String.format("Bearer %s", token));

    Map<String, Object> body = new HashMap<>();
    body.put("token", token);
    body.put("user", user);
    body.put("message", String.format("Hi, %s. You has initialized session successfully", user.getUsername()));

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
