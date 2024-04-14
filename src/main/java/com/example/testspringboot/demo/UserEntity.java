package com.example.testspringboot.demo;
<<<<<<< HEAD

import jakarta.persistence.*;

=======
import jakarta.persistence.*;
>>>>>>> 730b32ca4b65ef7fdcfd2423ba150e18b2d6b794
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Version
    private int version;
}