package com.example.dapanda.controller;

import com.example.dapanda.domain.product.Product;
import com.example.dapanda.domain.product.dto.ProductResponseInfo;
import com.example.dapanda.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/spring")
public class ProductController {
    private final ProductQueryService productQueryService;
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productQueryService.findAllProducts());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
        return ResponseEntity.ok(productQueryService.findProductByProductId(productId));
    }


    @GetMapping("/price/{productId}")
    public ResponseEntity<ProductResponseInfo.ProductPriceResDto> getPriceById(@PathVariable int productId) {
        return ResponseEntity.ok(productQueryService.findPriceByProductId(productId));
    }

    @GetMapping("/product/getAllPrice")
    public ResponseEntity<List<ProductResponseInfo.ProductAllPriceResDto>> getAllPrice() {
        return ResponseEntity.ok(productQueryService.getAllPrice());
    }

    @GetMapping("/product/getProductDetail/{productId}")
    public ResponseEntity<ProductResponseInfo.ProductDetailResDto> getProductDetail(@PathVariable int productId) {
        return ResponseEntity.ok(productQueryService.getProductDetail(productId));
    }
}
