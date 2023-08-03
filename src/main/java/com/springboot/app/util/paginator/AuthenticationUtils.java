package com.springboot.app.util.paginator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuthenticationUtils {

  public static Optional<Authentication> getAutentication(){
    return Optional.of(SecurityContextHolder.getContext().getAuthentication());
  }

}
