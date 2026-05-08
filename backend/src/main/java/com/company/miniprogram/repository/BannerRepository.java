package com.company.miniprogram.repository;

import com.company.miniprogram.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByStatusOrderBySortOrderAsc(int status);
}
