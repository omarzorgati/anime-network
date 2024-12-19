package com.omar.anime_network.services;


import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.UserResponse;
import com.omar.anime_network.entities.Role;
import com.omar.anime_network.entities.RoleName;
import com.omar.anime_network.entities.User;
import com.omar.anime_network.repositories.RoleRepository;
import com.omar.anime_network.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void elevateToAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRole().getName() == RoleName.ADMIN) {
            throw new IllegalStateException("User is already an admin");
        }

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));

        user.setRole(adminRole);
        userRepository.save(user);
    }


    public PageResponse<UserResponse> findAllUsers(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> userResponses = users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                userResponses,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }
    public UserResponse findById(Integer userId) {
        return userRepository.findById(userId)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + userId));
    }

    @Transactional
    public void deleteUserById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found with id " + userId);
        }
        userRepository.deleteById(userId);
    }
    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setEmail(user.getEmail());
        userResponse.setAccountLocked(user.getAccountLocked());
        userResponse.setEnabled(user.getEnabled());
        userResponse.setRole(user.getRole().getName().name());
        userResponse.setAvatar(user.getAvatar());
        userResponse.setCreatedDate(user.getCreatedDate());
        return userResponse;
    }
}
