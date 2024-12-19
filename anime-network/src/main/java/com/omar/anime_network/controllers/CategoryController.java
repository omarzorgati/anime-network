package com.omar.anime_network.controllers;


import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.CategoryRequest;
import com.omar.anime_network.dtos.CategoryResponse;
import com.omar.anime_network.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Category Management")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CategoryRequest request
    ) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("all-categories")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "sortField", defaultValue = "createdDate", required = false) String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC", required = false) String sortDirection
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size,sortField,sortDirection));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findUserById(@PathVariable("id") Integer categoryId){
        return ResponseEntity.ok(categoryService.findById(categoryId));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }




}
