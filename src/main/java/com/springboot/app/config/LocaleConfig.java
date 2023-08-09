package com.springboot.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

  public enum LANGUAGE {
    SPANISH("es"),
    ENGLISH("en"),
    GERMAN("de");

  private String langCode;

    LANGUAGE(String lang) {
      this.langCode = lang.toLowerCase();
    }

    public String getLangCode(){
      return this.langCode;
    }

    public static LANGUAGE of(String langCode){
      return switch (langCode.toLowerCase()){
        case "en" -> LANGUAGE.ENGLISH;
        case "es" -> LANGUAGE.SPANISH;
        case "de" -> LANGUAGE.GERMAN;
        default -> throw new IllegalArgumentException(String.format("Language %s unknown.", langCode));
      };
    }

  }

  private static final Map<LANGUAGE, Locale> localeMap = new HashMap<>();

  static {
    for(LANGUAGE lang : LANGUAGE.values()){
      String langCode = lang.getLangCode();
      String countyCode = lang.getLangCode().toUpperCase();
      localeMap.put(lang, new Locale(langCode, countyCode));
    }
  }


  @Bean
  public LocaleResolver localResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(localeMap.get(LANGUAGE.ENGLISH));
    return sessionLocaleResolver;
  }


  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
