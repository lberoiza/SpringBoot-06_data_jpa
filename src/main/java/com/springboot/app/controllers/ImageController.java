package com.springboot.app.controllers;

import com.springboot.app.services.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Controller
public class ImageController {

  @Autowired
  private IFileService fileService;

  private final Logger logger = LoggerFactory.getLogger(getClass());


//  @GetMapping(value = "/images/{filename:.+}")
  public ResponseEntity<Resource> showImage(@PathVariable String filename) {
    Path imagePath = fileService.getRootPathImages().resolve(filename).toAbsolutePath();
    logger.info("Pathimage: " + imagePath);

    Resource resource = null;
    String resourceFilename = "";
    try {
      resource = new UrlResource(imagePath.toUri());
      resourceFilename = resource.getFilename();
      if (!resource.exists() || !resource.isReadable()) {
        String error = String.format("The Image can't be loaded: '%s'", imagePath);
        logger.error(error);
        throw new RuntimeException(error);
      }

    } catch (MalformedURLException mfex) {
      logger.error(mfex.toString());
    }

    String header = String.format("attachment; filename=\"%s\"", resourceFilename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, header).body(resource);
  }
}
