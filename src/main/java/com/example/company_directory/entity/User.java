package com.example.company_directory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(length = 50)
    private String userId;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false)
    private int failedLoginCount = 0;

    private LocalDateTime lockedUntil;

    @Column(nullable = false)
    private boolean enabled = true;
}