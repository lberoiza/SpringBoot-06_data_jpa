package com.springboot.app.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public interface IFileService {
  public Path getRootPathImages();

  public Resource getImageAsResource(String imageName) throws MalformedURLException, NoSuchFileException;

  public String uploadImage(MultipartFile file);

  public boolean deleteImage(String imageName);
}
