package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    @Query("""
            SELECT t
            FROM Token t
            INNER JOIN User u
            ON t.user.id = u.id
            WHERE u.id = :userId
            AND (t.revoked = false OR t.expired = false)
            """)
    List<Token> findAllValidTokensByUserId(Integer userId);
}
