package com.springboot.app.auth.handlers;

import com.springboot.app.controllers.MainControler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;
import java.util.Locale;

// Componente que muestra un mensaje después de iniciar sesión
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final MessageSource messageSource;

  private final LocaleResolver localeResolver;

  @Autowired
  public LoginSuccessHandler(MessageSource messageSource, LocaleResolver localeResolver) {
    this.messageSource = messageSource;
    this.localeResolver = localeResolver;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    Locale locale = localeResolver.resolveLocale(request);
    String loginMessage = this.messageSource.getMessage("text.login.success", null, locale);

    FlashMap flashMap = new FlashMap();
    String successMsg = String.format(loginMessage, authentication.getName());
    flashMap.put("success", successMsg);

    SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
    flashMapManager.saveOutputFlashMap(flashMap, request, response);

//    super.setDefaultTargetUrl("/client/list");
    response.sendRedirect(MainControler.START_POINT);
    super.onAuthenticationSuccess(request, response, authentication);

  }
}
