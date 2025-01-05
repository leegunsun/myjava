package com.example.testspringboot.demo;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password_hash;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp create_at;

    @UpdateTimestamp
    @Column
    private Timestamp update_at;

//    @PrePersist
//    public void prePersist() {
//        if (this.testColum == null) {
//            this.testColum = "old_data";
//        }
//    }
}