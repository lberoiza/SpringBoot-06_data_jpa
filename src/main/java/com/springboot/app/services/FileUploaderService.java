package com.springboot.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Primary
@Service("FileUploaderService")
public class FileUploaderService implements IUploadFileService {

  private Path rootPathImages;


  public FileUploaderService(@Value("${dir.images}") String imageDir) {
    this.initializeImageRootPath(imageDir);
  }

  private void initializeImageRootPath(String imageDir) {
    this.rootPathImages = Paths.get(imageDir);
    createDirIfNotExists(this.rootPathImages.toFile());
  }

  @Override
  public String uploadImage(MultipartFile file) {
    return this.uploadFile(file, this.rootPathImages.toFile().getAbsolutePath());
  }

  @Override
  public Path getRootPathImages() {
    return rootPathImages;
  }


  private void createDirIfNotExists(File dir) {
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        String dirStr = dir.getAbsolutePath();
        throw new IllegalStateException(String.format("The Folder '%s' was not created.", dirStr));
      }
    }
  }

  private String uploadFile(MultipartFile file, String rootPath) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("The File is empty");
    }

    String uniqueFilename = this.createUniqueName(file.getOriginalFilename());
    try {
      Path pathDestination = Paths.get(String.format("%s/%s", rootPath, uniqueFilename));
      Files.copy(file.getInputStream(), pathDestination);
    } catch (Exception ex) {
      throw new IllegalStateException("It was a error by uploading");
    }

    return uniqueFilename;
  }

  private String createUniqueName(String filename) {
    String uuid = UUID.randomUUID().toString();
    return String.format("%s_%s", uuid, filename);
  }

}
