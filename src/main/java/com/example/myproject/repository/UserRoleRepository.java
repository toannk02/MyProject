package com.example.myproject.repository;

import com.example.myproject.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    @Modifying
    @Query("delete from UserRole ur where ur.user.id = :id")
    void deleteByUserId(@Param("id") int id);
}
