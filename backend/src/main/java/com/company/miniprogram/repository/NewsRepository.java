package com.company.miniprogram.repository;

import com.company.miniprogram.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByStatusOrderByCreatedAtDesc(int status, Pageable pageable);
}
