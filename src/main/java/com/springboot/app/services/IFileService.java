package com.springboot.app.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IFileService {
  public Path getRootPathImages();

  public String uploadImage(MultipartFile file);

  public boolean deleteImage(String imageName);
}
