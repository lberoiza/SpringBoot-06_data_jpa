package com.springboot.app.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.auth.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Primary
@Service("JsonWebTokenService")
public class JsonWebTokenService implements JWTService {

  public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  private static final int TOKEN_VALIDITY_IN_HOURS = 4;

  private final ObjectMapper jsonParser;

  public JsonWebTokenService() {
    this.jsonParser = new ObjectMapper();
  }

  @Override
  public String create(Authentication authentication) throws IOException {
    User user = (User) authentication.getPrincipal();

    Claims claims = Jwts.claims();
    claims.put("authorities", this.jsonParser.writeValueAsString(user.getAuthorities()));
    long millisecondPerHour = 3600000L;

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .signWith(SECRET_KEY)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_IN_HOURS * millisecondPerHour))
        .compact();
  }

  @Override
  public boolean validate(String jwtToken) {
    try {
      this.getClaims(jwtToken);
    } catch (RuntimeException ex) {
      return false;
    }
    return true;
  }

  @Override
  public String getUsername(String jwtToken) {
    return this.getClaims(jwtToken).getSubject();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities(String jwtToken) throws java.io.IOException {
    Object rolesJson = getClaims(jwtToken).get("authorities");
    return Arrays.asList(
        jsonParser.
            addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
            .readValue(rolesJson.toString(), SimpleGrantedAuthority[].class)
    );
  }

  @Override
  public String getJwtTokenFromRequest(HttpServletRequest request) throws IllegalArgumentException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Token not found or invalid");
    }
    return header.replace("Bearer ", "");
  }


  private Claims getClaims(String jwtToken) throws RuntimeException {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(jwtToken).getBody();
  }

}
