package com.example.testspringboot.demo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {

    private Long id;

    private String name;

    private String email;

    private int version;
}
