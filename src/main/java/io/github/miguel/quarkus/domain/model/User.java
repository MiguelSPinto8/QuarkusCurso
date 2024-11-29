package io.github.miguel.quarkus.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;


@Entity
@Table(name = "users")
@Data
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private Integer age;


}
