package com.example.company_directory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "persistent_logins") // Spring Security標準のテーブル名
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RememberMeToken {

    @Id
    @Column(length = 64) // 主キー（シリーズID）
    private String series;

    @Column(nullable = false, length = 64) // ユーザー名
    private String username;

    @Column(nullable = false, length = 64) // トークン値
    private String tokenValue;

    @Column(nullable = false) // 最終使用日時
    private LocalDateTime lastUsed;
}