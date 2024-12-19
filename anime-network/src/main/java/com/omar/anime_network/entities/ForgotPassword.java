package com.omar.anime_network.entities;



import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(nullable = false)
    private String otp;
    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;

}

