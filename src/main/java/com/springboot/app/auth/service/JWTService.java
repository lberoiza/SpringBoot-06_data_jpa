package com.springboot.app.auth.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;

public interface JWTService {

  String HEADER_AUTHORIZATION = "Authorization";
  String HEADER_TOKEN_PREFIX = "Bearer ";
  String RESPONSE_KEY_AUTHORITIES = "authorities";
  String RESPONSE_KEY_TOKEN = "token";
  String RESPONSE_KEY_USER = "user";

  String create(Authentication authentication) throws IOException;
  boolean validate(String jwtToken);
  String getUsername(String jwtToken);
  Collection<? extends GrantedAuthority> getAuthorities(String jwtToken) throws java.io.IOException;
  String getJwtTokenFromRequest(HttpServletRequest request);

}
