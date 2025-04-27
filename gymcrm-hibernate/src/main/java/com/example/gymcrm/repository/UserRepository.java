package com.example.gymcrm.repository;

import com.example.gymcrm.domain.User;

import java.util.Optional;

public interface UserRepository {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
