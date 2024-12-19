package com.omar.anime_network.services;

import com.omar.anime_network.dtos.ReviewRequest;
import com.omar.anime_network.entities.Anime;
import com.omar.anime_network.entities.Reviews;
import com.omar.anime_network.entities.User;
import com.omar.anime_network.exceptionHandler.customExceptions.AnimeNotFoundException;
import com.omar.anime_network.exceptionHandler.customExceptions.ReviewNotFoundException;
import com.omar.anime_network.repositories.AnimeRepository;
import com.omar.anime_network.repositories.ReviewsRepository;
import com.omar.anime_network.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;
    private final AnimeRepository animeRepository;
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
    public void addReview(ReviewRequest reviewRequest,Integer animeId) {
        User currentUser = getCurrentUser();
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundException("Anime not found with ID: " + animeId));

        Reviews review = Reviews.builder()
                .comment(reviewRequest.getComment())
                .rating(reviewRequest.getRating())
                .anime(anime)
                .user(currentUser)
                .build();

        reviewsRepository.save(review);
    }

    @Transactional
    public void updateReview(Integer reviewId, ReviewRequest reviewRequest) {
        Reviews review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

        User currentUser = getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to update this review");
        }

        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        reviewsRepository.save(review);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        Reviews review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

        User currentUser = getCurrentUser();
        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You are not authorized to delete this review");
        }

        reviewsRepository.delete(review);
    }
}
