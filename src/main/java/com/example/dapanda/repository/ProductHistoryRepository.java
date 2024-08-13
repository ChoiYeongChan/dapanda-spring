package com.example.dapanda.repository;

import com.example.dapanda.domain.productHistory.dto.ProductHistoryResponseInfo;
import com.example.dapanda.domain.productHistory.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory,Long> {
    @Query("SELECT p FROM ProductHistory p WHERE p.productId = :productId")
    ProductHistory findProductHistoryByProductId(int productId);
}
