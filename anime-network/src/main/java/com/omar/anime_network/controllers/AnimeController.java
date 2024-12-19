package com.omar.anime_network.controllers;

import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.AnimeRequest;
import com.omar.anime_network.dtos.AnimeResponse;
import com.omar.anime_network.entities.AnimeSeason;
import com.omar.anime_network.entities.AnimeStatus;
import com.omar.anime_network.entities.AnimeType;
import com.omar.anime_network.services.AnimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/anime")
@RequiredArgsConstructor
@Tag(name = "Anime Management")
public class AnimeController {

    private final AnimeService animeService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Void> createAnime(
            @RequestBody @Valid AnimeRequest anime
    ) {
        animeService.createAnime(anime);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping(value = "/upload-photos/{animeId}", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadAnimePhotos(
            @PathVariable("animeId") Integer animeId,
            @RequestPart("photos") List<MultipartFile> photos
    ) {
        animeService.uploadPhotos(animeId, photos);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{animeId}")
    public ResponseEntity<Void> updateAnime(
            @PathVariable("animeId") Integer animeId,
            @RequestBody @Valid AnimeRequest animeRequest
    ) {
        animeService.updateAnime(animeId, animeRequest);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{animeId}")
    public ResponseEntity<Void> deleteAnime(@PathVariable("animeId") Integer animeId) {
        animeService.deleteAnimeById(animeId);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AnimeResponse> findAnime(
            @PathVariable("id") Integer animeId) {
        return ResponseEntity.ok(animeService.findAnimeById(animeId));

    }
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<AnimeResponse>> filterAnimes(
            @RequestParam(name = "animeTitle", required = false) String animeTitle,
            @RequestParam(name = "season", required = false) AnimeSeason season,
            @RequestParam(name = "status", required = false) AnimeStatus status,
            @RequestParam(name = "animeType", required = false) AnimeType animeType,
            @RequestParam(name = "categories", required = false) List<String> categories,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(animeService.filterAnimes(animeTitle, season, status, animeType, categories, page, size));
    }
    @PostMapping("like/{animeId}")
    public ResponseEntity<Void> likeAnime(@PathVariable Integer animeId) {
        animeService.likeAnime(animeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/most-liked")
    public ResponseEntity<PageResponse<AnimeResponse>> getMostLikedAnimes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(animeService.getMostLikedAnimes(page, size));
    }





}
