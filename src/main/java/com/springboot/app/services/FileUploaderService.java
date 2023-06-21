package com.springboot.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Primary
@Service("FileUploaderService")
public class FileUploaderService implements IUploadFileService {

  private Path rootPathImages;


  public FileUploaderService(@Value("${static.images}") String imageDir) {
    this.initializeImageRootPath(imageDir);
  }

  private void initializeImageRootPath(String imageDir){
    this.rootPathImages = Paths.get(imageDir);
  }

  @Override
  public void uploadImage(MultipartFile file) {
    this.uploadFile(file, this.rootPathImages.toFile().getAbsolutePath());
  }

  @Override
  public Path getRootPathImages() {
    return rootPathImages;
  }

  private void uploadFile(MultipartFile file, String rootPath) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("The File is empty");
    }

    try {
      byte[] bytes = file.getBytes();
      Path pathDestination = Paths.get(String.format("%s//%s", rootPath, file.getOriginalFilename()));
      Files.write(pathDestination, bytes);
    } catch (Exception ex) {
      throw new IllegalStateException("It was a error by uploading");
    }

  }
}
