package com.springboot.app.util.paginator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticationUtils {

  public static Optional<Authentication> getAutentication() {
    return Optional.of(SecurityContextHolder.getContext().getAuthentication());
  }

  public static boolean hasRole(String roleName) {
    boolean hasRole = false;
    Optional<Authentication> optionalAuthentication = AuthenticationUtils.getAutentication();
    if (optionalAuthentication.isPresent()) {
      Authentication auth = optionalAuthentication.get();
      hasRole = auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    return hasRole;
  }


}
