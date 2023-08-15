package com.springboot.app.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public interface IFileService {
  Path getRootPathImages();

  Resource getImageAsResource(String imageName) throws MalformedURLException, NoSuchFileException;

  String uploadImage(MultipartFile file);

  boolean deleteImage(String imageName);
}
