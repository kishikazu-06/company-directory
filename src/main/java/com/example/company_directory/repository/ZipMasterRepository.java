package com.example.company_directory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.company_directory.entity.ZipMaster;

public interface ZipMasterRepository extends JpaRepository<ZipMaster, String> {
    // 郵便番号で検索
    ZipMaster findByZipCode(String zipCode);

    @Query("SELECT z FROM ZipMaster z WHERE CONCAT(z.prefecture, z.city, z.town) LIKE %:address%")
    List<ZipMaster> searchByAddress(@Param("address") String address);
}