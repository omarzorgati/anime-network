package com.omar.anime_network.controllers;


import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.UserResponse;
import com.omar.anime_network.services.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/elevate")
    public ResponseEntity<?> elevateUserToAdmin(@RequestParam String email) {
        adminService.elevateToAdmin(email);
        return ResponseEntity.ok("User elevated to admin");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-users")
    public ResponseEntity<PageResponse<UserResponse>> findAllUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortField", defaultValue = "createdDate", required = false) String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC", required = false) String sortDirection
    ) {
        return ResponseEntity.ok(adminService.findAllUsers(page, size, sortField, sortDirection));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{user-id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable("user-id") Integer userId){
        return ResponseEntity.ok(adminService.findById(userId));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete-user/{user-id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") Integer userId) {
        adminService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
