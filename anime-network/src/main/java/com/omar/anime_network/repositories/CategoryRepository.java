package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    List<Category> findByNameIn(List<String> names);
}
