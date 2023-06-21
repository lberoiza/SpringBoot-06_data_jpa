package com.springboot.app.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IUploadFileService {
  public Path getRootPathImages();

  public void uploadImage(MultipartFile file);
}
