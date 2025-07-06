package com.example.myproject.repository;

import com.example.myproject.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("select c from Cart c where (c.user.id is null or c.user.id = :userId) " +
            "and (c.status is null or c.status = :status) ")
    Cart findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);
}
