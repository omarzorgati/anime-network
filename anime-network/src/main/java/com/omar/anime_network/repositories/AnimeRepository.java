package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnimeRepository extends JpaRepository<Anime, Integer>, JpaSpecificationExecutor<Anime> {
    Page<Anime> findTop5ByOrderByLikesDesc(Pageable pageable);


}
