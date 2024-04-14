package com.example.testspringboot.demo;

<<<<<<< HEAD
public class UserRepository {

}
=======
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
//
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
>>>>>>> 730b32ca4b65ef7fdcfd2423ba150e18b2d6b794
