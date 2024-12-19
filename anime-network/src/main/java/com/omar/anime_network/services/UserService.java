package com.omar.anime_network.services;


import com.omar.anime_network.entities.User;
import com.omar.anime_network.filesManagement.FileStorageService;
import com.omar.anime_network.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No user is currently authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User not found"));
    }
    public void uploadUserAvatar(MultipartFile file) {
        User currentUser = getCurrentUser();
        String avatarPath = fileStorageService.saveUserAvatar(file, currentUser.getId());
        currentUser.setAvatar(avatarPath);
        userRepository.save(currentUser);

    }

}
