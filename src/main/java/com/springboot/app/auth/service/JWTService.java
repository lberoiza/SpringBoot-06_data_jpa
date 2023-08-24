package com.springboot.app.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface JWTService {

  String create(Authentication authentication) throws IOException;
  boolean validate(String jwtToken);
  String getUsername(String jwtToken);
  Collection<? extends GrantedAuthority> getAuthorities(String jwtToken) throws java.io.IOException;
  String getJwtTokenFromRequest(HttpServletRequest request);

}
