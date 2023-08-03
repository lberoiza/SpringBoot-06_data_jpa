package com.springboot.app.auth.handlers;

import com.springboot.app.controllers.MainControler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;

// Componente que muestra un mensaje después de iniciar sesión
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    FlashMap flashMap = new FlashMap();
    String successMsg = String.format("Wellcome, %s. You have initialized session successfully", authentication.getName());
    flashMap.put("success", successMsg);

    SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
    flashMapManager.saveOutputFlashMap(flashMap, request, response);

//    super.setDefaultTargetUrl("/client/list");
    response.sendRedirect(MainControler.START_POINT);
    super.onAuthenticationSuccess(request, response, authentication);

  }
}
