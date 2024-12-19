package com.omar.anime_network.controllers;

import com.omar.anime_network.dtos.ReviewRequest;
import com.omar.anime_network.services.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Review Management")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add/{animeId}")
    public ResponseEntity<Void> addReview(
            @RequestBody @Valid ReviewRequest reviewRequest,
            @PathVariable("animeId") Integer animeId
    ) {
        reviewService.addReview(reviewRequest,animeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Integer reviewId, @RequestBody @Valid ReviewRequest reviewRequest) {
        reviewService.updateReview(reviewId, reviewRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
