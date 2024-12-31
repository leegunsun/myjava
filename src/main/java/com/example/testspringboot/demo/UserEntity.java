package com.example.testspringboot.demo;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String testColum;

    @Version
    private int version;

    @PrePersist
    public void prePersist() {
        if (this.testColum == null) {
            this.testColum = "old_data";
        }
    }
}