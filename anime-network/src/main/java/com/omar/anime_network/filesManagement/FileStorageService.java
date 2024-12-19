package com.omar.anime_network.filesManagement;

import com.omar.anime_network.exceptionHandler.customExceptions.FileStorageException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.anime-photos-path}")
    private String animePhotosUploadPath;

    @Value("${application.file.upload.user-avatars-path}")
    private String userAvatarsUploadPath;

    /**
     * Save anime images for a specific anime title.
     */
    public List<String> saveAnimeImages(List<MultipartFile> images, String animeTitle) {
        validateImages(images);  // Validate multiple images
        String uploadSubPath = "animes" + File.separator + animeTitle.replaceAll("\\s+", "_") + File.separator + "images";
        return images.stream()
                .map(file -> saveFile(file, animePhotosUploadPath, uploadSubPath))
                .toList();
    }

    /**
     * Save a single user avatar using user ID.
     */
    public String saveUserAvatar(MultipartFile file, Integer userId) {
        validateAvatar(file);  // Validate avatar file type
        String uploadSubPath = "users" + File.separator + userId + File.separator + "avatars";
        return saveFile(file, userAvatarsUploadPath, uploadSubPath);
    }

    /**
     * Save a file to the local directory structure.
     */
    private String saveFile(@NonNull MultipartFile sourceFile, String baseUploadPath, String uploadSubPath) {
        final String finalUploadPath = baseUploadPath + File.separator + uploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            throw new FileStorageException("Failed to create directory: " + finalUploadPath);
        }

        String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved successfully to {}", targetFilePath);
            return targetPath.toString().replace("\\", "/");  // Ensure consistent file path format
        } catch (IOException e) {
            log.error("Failed to save file", e);
            throw new FileStorageException("Failed to save file: " + sourceFile.getOriginalFilename());
        }
    }

    /**
     * Validate multiple anime images.
     */
    private void validateImages(List<MultipartFile> images) {
        if (images.isEmpty() || images.size() > 6) {
            throw new FileStorageException("You must upload between 1 and 6 images.");
        }
        for (MultipartFile image : images) {
            validateFileExtension(image.getOriginalFilename(), "jpg", "jpeg", "png");
        }
    }

    /**
     * Validate a single user avatar file.
     */
    private void validateAvatar(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("You must upload a valid image file.");
        }
        validateFileExtension(file.getOriginalFilename(), "jpg", "jpeg", "png");
    }

    /**
     * Check file extensions.
     */
    private void validateFileExtension(String fileName, String... allowedExtensions) {
        String fileExtension = getFileExtension(fileName);
        if (fileExtension == null || !List.of(allowedExtensions).contains(fileExtension)) {
            throw new FileStorageException("Invalid file type. Allowed types are: " + String.join(", ", allowedExtensions));
        }
    }

    /**
     * Extract the file extension from the file name.
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
