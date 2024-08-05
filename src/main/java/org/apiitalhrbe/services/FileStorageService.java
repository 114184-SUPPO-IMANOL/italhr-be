package org.apiitalhrbe.services;

import org.apiitalhrbe.configs.FileStorageProperties;
import org.apiitalhrbe.exceptions.FileStorageException;
import org.apiitalhrbe.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    public String storeFile(MultipartFile file, String fileName) {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalName.substring(originalName.lastIndexOf("."));

        if (fileName.isEmpty()) {
            fileName = UUID.randomUUID().toString();
        }

        Path fileStorageLocation = getFileStorageLocation(getFolderName(originalName));
        Path targetLocation = fileStorageLocation.resolve(fileName + extension);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName);
        }
    }

    private String getFolderName(String completeFileName) {
        String extension = completeFileName.substring(completeFileName.lastIndexOf("."));
        return extension.replace(".", "").toUpperCase();
    }

    private Path getFileStorageLocation(String folderName) {
        Path fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir() + "/" + folderName).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStorageLocation);
            return fileStorageLocation;
        } catch (IOException e) {
            throw new FileStorageException("Could not create directory");
        }
    }

    public Resource loadResource(String completeFileName) {
        Path fileStorageLocation = getFileStorageLocation(getFolderName(completeFileName));
        Path path = fileStorageLocation.resolve(completeFileName).normalize();
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found: " + completeFileName);
            }

        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("Error while trying to access the file: " + completeFileName);
        }
    }

    public boolean deleteFile(String completeFileName) {
        boolean deleted = false;
        Path targetLocation = getFileStorageLocation(getFolderName(completeFileName)).resolve(completeFileName).normalize();
        try {
            Files.deleteIfExists(targetLocation);
            deleted = true;
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file");
        }
        return deleted;
    }
}
