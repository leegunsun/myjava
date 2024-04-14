package com.example.testspringboot.demo;


import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
//
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

