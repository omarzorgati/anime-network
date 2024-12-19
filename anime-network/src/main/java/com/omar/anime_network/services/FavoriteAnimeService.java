package com.omar.anime_network.services;


import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.AnimeResponse;
import com.omar.anime_network.entities.Anime;
import com.omar.anime_network.entities.Category;
import com.omar.anime_network.entities.FavoriteAnime;
import com.omar.anime_network.entities.User;
import com.omar.anime_network.exceptionHandler.customExceptions.AnimeNotFoundException;
import com.omar.anime_network.repositories.AnimeRepository;
import com.omar.anime_network.repositories.FavoriteAnimeRepository;
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
public class FavoriteAnimeService {
    private final FavoriteAnimeRepository favoriteRepository;
    private final AnimeRepository animeRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No user is currently authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalStateException("User not found"));
    }
    @Transactional
    public void addToFavorites(Integer animeId) {
        User currentUser = getCurrentUser();
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime with ID " + animeId + " not found"));

        if (favoriteRepository.findByUserAndAnime(currentUser, anime).isEmpty()) {
            FavoriteAnime favorite = FavoriteAnime.builder()
                    .user(currentUser)
                    .anime(anime)
                    .build();
            favoriteRepository.save(favorite);
        }
    }
    @Transactional
    public void removeFromFavorites(Integer animeId) {
        User currentUser = getCurrentUser();
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime with ID " + animeId + " not found"));

        favoriteRepository.deleteByUserAndAnime(currentUser, anime);
    }

    public PageResponse<AnimeResponse> getFavoriteAnimes(int page, int size) {
        User currentUser = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<FavoriteAnime> favorites = favoriteRepository.findByUser(currentUser, pageable);
        List<AnimeResponse> animeResponses = favorites.stream()
                .map(favoriteAnime -> mapToAnimeResponse(favoriteAnime.getAnime()))
                .collect(Collectors.toList());

        return new PageResponse<>(
                animeResponses,
                favorites.getNumber(),
                favorites.getSize(),
                favorites.getTotalElements(),
                favorites.getTotalPages(),
                favorites.isFirst(),
                favorites.isLast()
        );
    }

    private AnimeResponse mapToAnimeResponse(Anime anime) {
        AnimeResponse animeResponse = new AnimeResponse();
        animeResponse.setTitle(anime.getTitle());
        animeResponse.setDescription(anime.getDescription());
        animeResponse.setAuthor(anime.getAuthor());
        animeResponse.setSeasonNumber(anime.getSeasonNumber());
        animeResponse.setImages(anime.getImages());
        animeResponse.setSeason(anime.getSeason());
        animeResponse.setType(anime.getType());
        animeResponse.setReleaseDate(anime.getReleaseDate());
        animeResponse.setStatus(anime.getStatus());

        animeResponse.setCategoryNames(
                anime.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet())
        );

        return animeResponse;
    }

}
