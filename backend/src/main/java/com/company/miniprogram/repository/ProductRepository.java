package com.company.miniprogram.repository;

import com.company.miniprogram.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.status = 1 AND " +
           "('' = :keyword OR :keyword IS NULL OR p.name LIKE %:keyword% OR p.productNo LIKE %:keyword%) AND " +
           "('' = :categoryId OR :categoryId IS NULL OR p.categoryId = :categoryId)")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                  @Param("categoryId") String categoryId,
                                  Pageable pageable);
}
