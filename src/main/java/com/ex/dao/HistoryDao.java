package com.ex.dao;

import com.ex.entity.History;
import com.ex.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryDao extends JpaRepository<History, Integer> {

    @Query("select u from History u where u.users.username=:username")
    List<History> findByUsername(@Param("username")String username);

}
