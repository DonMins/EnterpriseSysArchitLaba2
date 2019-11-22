package com.ex.dao;
import com.ex.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingsDao extends JpaRepository<Rating, Integer> {

    @Query("select u from Rating u where u.users.username=:username")
    Rating findByUsername(@Param("username")String username);

    @Query("select max(u.countgame) from Rating u where u.users.username=:username")
    Integer findMaxGameNumber(@Param("username")String username);
}
