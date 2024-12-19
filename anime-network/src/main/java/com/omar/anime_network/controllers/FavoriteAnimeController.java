package com.omar.anime_network.controllers;

import com.omar.anime_network.common.PageResponse;
import com.omar.anime_network.dtos.AnimeResponse;
import com.omar.anime_network.services.FavoriteAnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteAnimeController {

    private final FavoriteAnimeService favoriteService;

    @PostMapping("/add/{animeId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Integer animeId) {
        favoriteService.addToFavorites(animeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{animeId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Integer animeId) {
        favoriteService.removeFromFavorites(animeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<PageResponse<AnimeResponse>> getFavoriteAnimes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(favoriteService.getFavoriteAnimes(page, size));
    }
}

