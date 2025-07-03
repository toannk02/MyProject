package com.example.myproject.repository;

import com.example.myproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "(:keyWord IS NULL OR LOWER(u.username) LIKE CONCAT('%', LOWER(:keyWord), '%')) " +
            "AND (:username IS NULL OR LOWER(u.username) = LOWER(:username)) " +
            "AND (:active IS NULL OR u.active = :active)")
    Page<User> search(
            @Param("keyWord") String keyWord,
            @Param("username") String username,
            @Param("active") Boolean active,
            Pageable pageable);

    boolean existsByUsername(String username);
}
