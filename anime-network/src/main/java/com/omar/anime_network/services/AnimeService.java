package com.omar.anime_network.services;

import com.omar.anime_network.common.AnimeSpecification;
import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.AnimeRequest;
import com.omar.anime_network.dtos.AnimeResponse;
import com.omar.anime_network.dtos.CategoryResponse;
import com.omar.anime_network.entities.*;
import com.omar.anime_network.exceptionHandler.customExceptions.AnimeNotFoundException;
import com.omar.anime_network.exceptionHandler.customExceptions.CategoryNotFoundException;
import com.omar.anime_network.filesManagement.FileStorageService;
import com.omar.anime_network.repositories.AnimeRepository;
import com.omar.anime_network.repositories.CategoryRepository;
import com.omar.anime_network.repositories.FavoriteAnimeRepository;
import com.omar.anime_network.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final FavoriteAnimeRepository animeFavoriteRepository;
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
    public void createAnime(AnimeRequest request) {
        Set<Category> categories = fetchCategoriesByNames(request.getCategoryNames());
        Anime anime = Anime.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(request.getAuthor())
                .seasonNumber(request.getSeasonNumber())
                .season(request.getSeason())
                .type(request.getType())
                .releaseDate(request.getReleaseDate())
                .status(request.getStatus())
                .categories(categories)
                .build();

        animeRepository.save(anime);
    }
    private Set<Category> fetchCategoriesByNames(List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByNameIn(categoryNames);
        if (categories.size() != categoryNames.size()) {
            throw new CategoryNotFoundException("Some categories do not exist for the provided names: " + categoryNames);
        }
        return new HashSet<>(categories);
    }
    @Transactional
    public void uploadPhotos(Integer animeId, List<MultipartFile> photos) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with ID: " + animeId));

        List<String> photoPaths = fileStorageService.saveAnimeImages(photos, anime.getTitle());
        anime.setImages(String.join(",", photoPaths)); // Store paths as a comma-separated string
        animeRepository.save(anime);
    }
    @Transactional
    public void updateAnime(Integer animeId, AnimeRequest request) {
        Anime existingAnime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with ID: " + animeId));

        Set<Category> categories = fetchCategoriesByNames(request.getCategoryNames());

        existingAnime.setTitle(request.getTitle());
        existingAnime.setDescription(request.getDescription());
        existingAnime.setAuthor(request.getAuthor());
        existingAnime.setSeasonNumber(request.getSeasonNumber());
        existingAnime.setSeason(request.getSeason());
        existingAnime.setType(request.getType());
        existingAnime.setReleaseDate(request.getReleaseDate());
        existingAnime.setStatus(request.getStatus());
        existingAnime.setCategories(categories);
        animeRepository.save(existingAnime);
    }

    public void deleteAnimeById(Integer id) {
        if (!animeRepository.existsById(id)) {
            throw new CategoryNotFoundException("Anime with ID " + id + " not found");
        }
        animeFavoriteRepository.deleteByAnimeId(id);
        animeRepository.deleteById(id);
    }
    public AnimeResponse findAnimeById(Integer AnimeId) {
        return animeRepository.findById(AnimeId)
                .map(this::mapToAnimeResponse)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with ID: " + AnimeId));

    }
    public PageResponse<AnimeResponse> filterAnimes(
            String title,
            AnimeSeason season,
            AnimeStatus status,
            AnimeType type,
            List<String> categories,
            int page,
            int size
    ) {
        Specification<Anime> spec = Specification.where(AnimeSpecification.hasTitle(title))
                .and(AnimeSpecification.hasSeason(season))
                .and(AnimeSpecification.hasStatus(status))
                .and(AnimeSpecification.hasType(type))
                .and(AnimeSpecification.hasCategories(categories));

        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseDate").descending());

        Page<Anime> anime = animeRepository.findAll(spec, pageable);

        List<AnimeResponse> animeResponses = anime.stream()
                .map(this::mapToAnimeResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                animeResponses,
                anime.getNumber(),
                anime.getSize(),
                anime.getTotalElements(),
                anime.getTotalPages(),
                anime.isFirst(),
                anime.isLast()
        );
    }

    @Transactional
    public void likeAnime(Integer animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with ID: " + animeId));

        anime.setLikes(anime.getLikes() + 1);
        animeRepository.save(anime);
    }
    public PageResponse<AnimeResponse> getMostLikedAnimes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());
        Page<Anime> animes = animeRepository.findTop5ByOrderByLikesDesc(pageable);

        List<AnimeResponse> animeResponses = animes.stream()
                .map(this::mapToAnimeResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                animeResponses,
                animes.getNumber(),
                animes.getSize(),
                animes.getTotalElements(),
                animes.getTotalPages(),
                animes.isFirst(),
                animes.isLast()
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

        Set<String> categoryNames = anime.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
        animeResponse.setCategoryNames(categoryNames);
        animeResponse.setLikes(anime.getLikes());

        return animeResponse;
    }





}







