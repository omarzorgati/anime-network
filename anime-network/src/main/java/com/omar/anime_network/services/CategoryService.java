package com.omar.anime_network.services;


import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.CategoryRequest;
import com.omar.anime_network.dtos.CategoryResponse;
import com.omar.anime_network.entities.Anime;
import com.omar.anime_network.entities.Category;
import com.omar.anime_network.entities.User;
import com.omar.anime_network.exceptionHandler.customExceptions.CategoryAlreadyExistsException;
import com.omar.anime_network.exceptionHandler.customExceptions.CategoryNotFoundException;
import com.omar.anime_network.repositories.CategoryRepository;
import com.omar.anime_network.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    private  User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No user is currently authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User not found"));
    }
    @Transactional
    public void createCategory(CategoryRequest request) {
        // Check if a category with the same name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryAlreadyExistsException("Category with name '" + request.getName() + "' already exists.");
        }
        User currentUser = getCurrentUser();
        Category category = Category.builder()
                .name(request.getName())
                .createdBy(currentUser)
                .build();
      categoryRepository.save(category);
    }
    @Transactional
    public void updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " not found"));
        if (categoryRepository.existsByName(request.getName())) {
            throw new CategoryAlreadyExistsException("Category with name '" + request.getName() + "' already exists.");
        }
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " not found"));
        // Manually remove associations in the join table
        for (Anime anime : category.getAnimes()) {
            anime.getCategories().remove(category); // Remove the category from each anime
        }
        category.getAnimes().clear(); // Clear all anime references in this category
        categoryRepository.delete(category);
    }

    public PageResponse<CategoryResponse> getAllCategories(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);

        List<CategoryResponse> categoryResponses = categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                categoryResponses,
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.isFirst(),
                categories.isLast()
        );
    }
    public CategoryResponse findById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .map(this::mapToCategoryResponse)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + categoryId));
    }

    public CategoryResponse getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(this::mapToCategoryResponse)
                .orElseThrow(() -> new CategoryNotFoundException("category not found with name " + name));
    }




    private CategoryResponse mapToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setCreatedDate(category.getCreatedDate());
        if (category.getCreatedBy() != null) {
            categoryResponse.setCreatedBy(category.getCreatedBy().fullName());
        } else {
            categoryResponse.setCreatedBy(null);
        }
        categoryResponse.setAnimeCount(category.getAnimes() != null ? category.getAnimes().size() : 0);
        return categoryResponse;

    }






}
