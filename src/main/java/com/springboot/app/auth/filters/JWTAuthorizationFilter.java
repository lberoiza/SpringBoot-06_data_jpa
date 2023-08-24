package com.springboot.app.auth.filters;

import com.springboot.app.auth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final JWTService jwtService;

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
    super(authenticationManager);
    this.jwtService = jwtService;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {

    String header = request.getHeader(JWTService.HEADER_AUTHORIZATION);
    if (!requiresAuthentication(header)) {
      chain.doFilter(request, response);
      return;
    }

    String jwtToken = this.jwtService.getJwtTokenFromRequest(request);


    UsernamePasswordAuthenticationToken authentication = null;
    if(this.jwtService.validate(jwtToken)) {
      logger.info("The Token is valid");
      String username = this.jwtService.getUsername(jwtToken);
      authentication = new UsernamePasswordAuthenticationToken(username, null, this.jwtService.getAuthorities(jwtToken));
    }

    // Registra en el contexto de seguridad la autenticacion para este request
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);

  }

  protected boolean requiresAuthentication(String header) {
    return header != null && header.startsWith("Bearer ");
  }

}
