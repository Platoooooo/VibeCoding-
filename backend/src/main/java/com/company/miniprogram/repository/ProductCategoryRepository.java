package com.company.miniprogram.repository;

import com.company.miniprogram.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    List<ProductCategory> findByStatusOrderBySortOrderAsc(int status);
}
