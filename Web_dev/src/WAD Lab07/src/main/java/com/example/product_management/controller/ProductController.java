package com.example.product_management.controller;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // List all products
@GetMapping
public String listProducts(
        @RequestParam(required = false) String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String category,
        Model model) {

    List<Product> products;

    Sort sort = Sort.unsorted();
    if (sortBy != null) {
        sort = sortDir.equals("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
    }

    if (category != null && !category.isEmpty()) {
        products = productService.getProductsByCategory(category, sort);
    } else {
        products = productService.getAllProducts(sort);
    }

    model.addAttribute("products", products);
    model.addAttribute("sortBy", sortBy);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("category", category);

    model.addAttribute("categories", productService.getAllCategories()); // For dropdown

    return "product-list";
}

    // Show form for new product
    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }

    // Show form for editing product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }

    // Save product (create or update)
@PostMapping("/save")
public String saveProduct(
    @Valid @ModelAttribute("product") Product product,
    BindingResult result,
    Model model,
    RedirectAttributes redirectAttributes) {
    
    if (product.getProductCode() == null || 
        !product.getProductCode().matches("^P\\d{4}$")) {
        
        result.rejectValue("productCode", "invalid.code",
                "Product code must match format P1234");
    }

    if (product.getName() == null || product.getName().isBlank()) {
        result.rejectValue("name", "invalid.name", "Name is required");
    }

    if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
        result.rejectValue("price", "invalid.price", "Price must be greater than 0");
    }

    if (product.getQuantity() < 0) {
        result.rejectValue("quantity", "invalid.quantity", "Quantity cannot be negative");
    }

    // If any errors exist, return form
    if (result.hasErrors()) {
        return "product-form";
    }
    
    try {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
    }
    
    return "redirect:/products";
}
    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/advanced-search")
    public String advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // defensive: ensure non-negative page and positive size
        if (page < 0)
            page = 0;
        if (size <= 0)
            size = 10;

        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productService.advancedSearch(name, category, minPrice, maxPrice, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber()); // 0-based
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());

        // preserve filter values in form and links
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("size", size); // so links can keep same size
        System.out.println("totalPages=" + productPage.getTotalPages());
        System.out.println("totalElements=" + productPage.getTotalElements());

        return "product-list";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProducts(keyword, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword); // keep search text

        return "product-list";
    }

}
