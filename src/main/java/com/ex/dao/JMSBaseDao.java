package com.ex.dao;

import com.ex.entity.JMSBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JMSBaseDao extends JpaRepository<JMSBase, Integer> {

    @Query("select u from JMSBase u where u.users.username=:username")
    JMSBase findByUsername(@Param("username")String username);
}
