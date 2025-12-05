package com.example.product_management.service;

import com.example.product_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    List<Product> searchProducts(String keyword);

    List<Product> getProductsByCategory(String category);

    List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    Page<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable);

    List<Product> getAllProducts(Sort sort);

    List<Product> searchProducts(String keyword, Sort sort);

    long countByCategory(String category);

    BigDecimal getTotalInventoryValue();

    BigDecimal getAveragePrice();

    List<Product> getLowStockProducts(int threshold);

    List<Product> getRecentProducts(int limit);

    List<Product> getProductsByCategory(String category, Sort sort);

    List<String> getAllCategories();

}
