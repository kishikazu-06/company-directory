package com.example.company_directory.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class CompanySearchForm {
    // キーワード検索用（企業名、住所などを横断検索）
    private String keyword;

    // 詳細検索用
    private Integer companyId;
    private String companyName;
    private String address;
    private String zipCode;

    // 登録日（範囲検索）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

}