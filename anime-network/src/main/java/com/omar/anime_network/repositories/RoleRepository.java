package com.omar.anime_network.repositories;

import com.omar.anime_network.entities.Role;
import com.omar.anime_network.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName role);
}
