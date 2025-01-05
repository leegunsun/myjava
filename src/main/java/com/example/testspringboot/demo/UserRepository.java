package com.example.testspringboot.demo;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 네이티브 SQL 쿼리: 특정 이름 검색
    @Query(value = "SELECT * FROM users WHERE username = :name", nativeQuery = true)
    List<UserEntity> findByName(@Param("name") String name);

}

