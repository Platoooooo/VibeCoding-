package com.company.miniprogram.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String image;

    @Column(name = "parent_id", length = 50)
    private String parentId;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
