package com.example.product_management.controller;

import com.example.product_management.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String showDashboard(Model model) {

        model.addAttribute("totalValue", productService.getTotalInventoryValue());
        model.addAttribute("avgPrice", productService.getAveragePrice());
        model.addAttribute("lowStock", productService.getLowStockProducts(10));
        model.addAttribute("recentProducts", productService.getRecentProducts(5));

        // Example categories (you may fetch dynamically)
        model.addAttribute("electronicsCount", productService.countByCategory("Electronics"));
        model.addAttribute("clothingCount", productService.countByCategory("Clothing"));
        model.addAttribute("foodCount", productService.countByCategory("Food"));

        return "dashboard";
    }
}
