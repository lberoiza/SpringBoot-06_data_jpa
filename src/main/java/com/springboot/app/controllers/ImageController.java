package com.springboot.app.controllers;

import com.springboot.app.services.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;

@Controller
public class ImageController {

  @Autowired
  private IFileService fileService;

  private final Logger logger = LoggerFactory.getLogger(getClass());


//  @GetMapping(value = "/images/{filename:.+}")
  public ResponseEntity<Resource> showImage(@PathVariable String filename) {
    Resource resource = null;
    String resourceFilename = "";

    try {
      resource = fileService.getImageAsResource(filename);
    } catch (MalformedURLException e) {
      logger.error("Malformed Exception");
      logger.error(e.toString());
    } catch (NoSuchFileException e) {
      logger.error("Not founded Image Exception");
      logger.error(e.toString());
    }

    String header = String.format("attachment; filename=\"%s\"", resourceFilename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, header).body(resource);
  }
}
