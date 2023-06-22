package com.springboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySources({
    @PropertySource("classpath:config.properties")
})
public class Config implements WebMvcConfigurer {

  @Value("${url.images}")
  private String urlImages;


  @Value("${dir.images}")
  private String dirImages;

//
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    WebMvcConfigurer.super.addResourceHandlers(registry);
//    registry.addResourceHandler(getResourceHandlerSrt(urlImages))
//        .addResourceLocations(getResourceLocationSrt(dirImages));
//  }

  private String getResourceHandlerSrt(String url){
    return String.format("%s/**", url);
  }

  private String getResourceLocationSrt(String dir){
    return String.format("file:%s/", dir);
  }


}
