package com.example.company_directory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "login_attempts") // テーブル名を指定
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {

    @Id
    @Column(length = 45) // IPv6 (最長39文字) + 余裕を見て45文字
    private String ipAddress;

    @Column(nullable = false)
    private int failureCount;

    @Column(nullable = false)
    private LocalDateTime lastAttemptTime;
}