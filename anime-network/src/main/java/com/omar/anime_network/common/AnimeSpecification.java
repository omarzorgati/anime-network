package com.omar.anime_network.common;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import com.omar.anime_network.entities.Anime;
import com.omar.anime_network.entities.AnimeSeason;
import com.omar.anime_network.entities.AnimeStatus;
import com.omar.anime_network.entities.AnimeType;

import java.util.Date;
import java.util.List;

public class AnimeSpecification {

    public static Specification<Anime> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Anime> hasSeason(AnimeSeason season) {
        return (root, query, criteriaBuilder) ->
                season == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("season"), season);
    }

    public static Specification<Anime> hasStatus(AnimeStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Anime> hasType(AnimeType animeType) {
        return (root, query, criteriaBuilder) ->
                animeType == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("type"), animeType);
    }

    public static Specification<Anime> hasCategories(List<String> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> categoryJoin = root.join("categories");
            return categoryJoin.get("name").in(categories);
        };
    }

}

