package com.omar.anime_network.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 1024)
    private String token;

    private boolean expired;
    private boolean revoked;



    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
