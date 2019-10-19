package com.ex.dao;

import com.ex.entity.Changes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangesDao extends JpaRepository<Changes,Integer> {
}
