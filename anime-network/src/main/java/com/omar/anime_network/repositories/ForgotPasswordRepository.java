package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.ForgotPassword;
import com.omar.anime_network.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query(
            """
            SELECT fp
            FROM ForgotPassword fp
            WHERE fp.otp= ?1
            AND fp.user= ?2
            """
    )
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    Optional<ForgotPassword> findByUser(User user);
}
