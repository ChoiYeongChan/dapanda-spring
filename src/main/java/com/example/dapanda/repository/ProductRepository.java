package com.example.dapanda.repository;

import com.example.dapanda.domain.product.Product;
import com.example.dapanda.domain.product.dto.ProductResponseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    //List<Product> findAll();
    Product findByProductId(int productId);

    @Query("SELECT p.productId, p.highestPrice FROM Product p")
    List<ProductResponseInfo.ProductAllPriceResDto> findAllPrice();
}
