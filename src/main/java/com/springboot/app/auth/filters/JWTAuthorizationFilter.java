package com.springboot.app.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.auth.SimpleGrantedAuthoritiesMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final ObjectMapper jsonParser;



  public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
    this.jsonParser = new ObjectMapper();
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {

    String header = request.getHeader("Authorization");
    if (!requiresAuthentication(header)) {
      chain.doFilter(request, response);
      return;
    }


    boolean isTokenValid = false;
    Claims claims = null;
    try {
      claims = Jwts.parserBuilder()
          .setSigningKey(JWTAuthenticationFilter.SECRET_KEY)
          .build()
          .parseClaimsJws(header.replace("Bearer ", "")).getBody();
      isTokenValid = true;
    } catch (JwtException | IllegalArgumentException e) {
      logger.error(e.toString());
    }

    UsernamePasswordAuthenticationToken authentication = null;
    if(isTokenValid && claims != null) {
      logger.info("Token valid");
      String username = claims.getSubject();
      Object rolesJson = claims.get("authorities");

      Collection<? extends GrantedAuthority> authorities = Arrays.asList(
          jsonParser.
              addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesMixin.class)
              .readValue(rolesJson.toString(), SimpleGrantedAuthority[].class)
      );
      authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    // Registra en el contexto de seguridad la autenticacion para este request
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);

  }

  protected boolean requiresAuthentication(String header) {
    return header != null && header.startsWith("Bearer ");
  }

}
