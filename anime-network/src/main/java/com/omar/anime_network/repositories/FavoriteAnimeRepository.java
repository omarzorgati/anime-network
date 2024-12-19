package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.Anime;
import com.omar.anime_network.entities.FavoriteAnime;
import com.omar.anime_network.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface FavoriteAnimeRepository extends JpaRepository<FavoriteAnime, Integer> {
    Optional<FavoriteAnime> findByUserAndAnime(User user, Anime anime);

    void deleteByUserAndAnime(User user, Anime anime);

    Page<FavoriteAnime> findByUser(User currentUser, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByAnimeId(Integer animeId);
}

